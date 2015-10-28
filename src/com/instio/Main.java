package com.instio;

import spark.ModelAndView;
import spark.Session;
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
            "/",
            ((request, response) -> {
                Session session = request.session();
                String username = session.attribute("username");//read username
                if (username == null) { //if user is not logged in
                    return new ModelAndView(new HashMap(), "not-logged-in.html");
                }
                HashMap m = new HashMap();
                m.put("username", username);
                return new ModelAndView(m, "logged-in.html");
            }),

                Spark.post(
                "/create-post",
            ((request1, response1) -> {
                String username = request1.queryParams("username");
                Session session = request1.session();
                session.attribute("username", username);
                response1.redirect("/"); // "/" represents the top level. return page posted
                return response1 + ""; //keep adding posts
            })
        );
            Spark.get(
                "/posts",
            ((request, response) -> {
                HashMap m = new HashMap();
                m.put("name", user.name);
                m.put("posts", posts);
                return new ModelAndView(m, "/");
            }),
            new MustacheTemplateEngine()
            );
        }
    }
