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
        Spark.staticFileLocation("public");
        Spark.init();


            Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("username");//read username
                    if (name == null) { //if user is not logged in
                        return new ModelAndView(new HashMap(), "index.html");
                    }
                    HashMap m = new HashMap();
                    m.put("username", name);
                    m.put("posts", posts);
                    return new ModelAndView(m, "post.html");
                }),
                    new MustacheTemplateEngine()
            );
            Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("name");
                    Session session = request.session();
                    session.attribute("username", name);
                    response.redirect("/"); // "/" represents the top level. return page posted
                    return ""; //keep adding posts
                })
             );
            Spark.post(
                    "/create-post",
                    ((request, response) -> {
                       Post post = new Post();
                        post.id = posts.size() + 1;
                        post.text = request.queryParams("text");
                        posts.add(post);
                        response.redirect("/");
                        return "";
                    })
            );
             Spark.post(
                     "/delete-beer",
                     ((request, response) -> {
                         String id = request.queryParams("postid");
                         try {
                             int idNum = Integer.valueOf(id);
                             posts.remove(idNum - 1);
                             for (int i = 0; i < posts.size(); i++) {
                                 posts.get(i).id = i + 1; //changes the number when you delete a beer
                             }
                         } catch (Exception e) {

                         }
                         response.redirect("/");
                         return "";
                     })
             );
            Spark.post(
                    "/edit-beer",
                    ((request, response) -> {
                        String id = request.queryParams("postid");
                        try {
                            int idNum = Integer.valueOf(id);
                            Post post = posts.get(idNum - 1);
                            post.text = request.queryParams("edit-post");
                        } catch (Exception e) {

                        }
                        response.redirect("/");
                        return "";
                    })
            );
        }
    }
