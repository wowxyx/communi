package com.houke.community.controller;

import com.houke.community.entity.Page;
import com.houke.community.entity.User;
import com.houke.community.service.FollowService;
import com.houke.community.service.UserService;
import com.houke.community.util.CommunityConstant;
import com.houke.community.util.CommunityUtil;
import com.houke.community.util.HostHolder;
import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/follow",method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType,int entityId){
        User user = hostHolder.getUser();

        followService.follow(user.getId(),entityType,entityId);

        return CommunityUtil.getJSONString(0,"已关注！");
    }

    @RequestMapping(path = "/unFollow",method = RequestMethod.POST)
    @ResponseBody
    public String unFollow(int entityType,int entityId){
        User user = hostHolder.getUser();

        followService.unFollow(user.getId(),entityType,entityId);

        return CommunityUtil.getJSONString(0,"已取消关注！");
    }

    @RequestMapping(path = "/followees/{userId}",method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId") int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user",user);

        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId,ENTITY_TYPE_USER));

        List<Map<String,Object>> userList = followService.findFollowees(userId,page.getOffset(),page.getLimit());
        if (userList != null) {
            for (Map<String,Object> map :userList) {
                User u = (User)map.get("user");
                map.put("hasFollowed",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);

        return "/site/followee";
    }

    @RequestMapping(path = "/followers/{userId}",method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user",user);

        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER,userId));

        List<Map<String,Object>> userList = followService.findFollowers(userId,page.getOffset(),page.getLimit());
        if (userList != null) {
            for (Map<String,Object> map :userList) {
                User u = (User)map.get("user");
                map.put("hasFollowed",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);

        return "/site/follower";
    }

    private boolean hasFollowed(int userId) {
        if (hostHolder.getUser() == null) {
            return false;
        }

        return followService.hasFollowed(hostHolder.getUser().getId(),ENTITY_TYPE_USER,userId);
    }

}
