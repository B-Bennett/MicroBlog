package com.instio;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ArrayList<Post> posts = new ArrayList();
        User user = new User();
        Spark.staticFileLocation("public");
        Spark.init();
        Spark.post(
                "/create-user",
                ((request, response) -> {
                    user.name = request.queryParams("username");
                    response.redirect("/posts");
                    return "";
                })
        );
        Spark.post(
                "/create-post",
                ((request1, response1) -> {
                    Post post = new Post();
                    post.text = request1.queryParams("postText"); //name in my post.html
                    posts.add(post);
                    response1.redirect("/posts"); // "/" represents the top level. return page posted
                    return response1 + ""; //keep adding posts
                })
        );
        Spark.get(
                "/posts",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    m.put("name", user.name);
                    m.put("posts", posts);
                    return new ModelAndView(m, "posts.html");
                }),
                new MustacheTemplateEngine()
        );
    }
}
