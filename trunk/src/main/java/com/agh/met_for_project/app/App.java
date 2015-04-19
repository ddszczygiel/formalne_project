package com.agh.met_for_project.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.agh.met_for_project")
public class App {

        public static void main(String[] args) {

            ConfigurableApplicationContext applicationContext = SpringApplication.run(App.class, args);
//            for ( String s : applicationContext.getBeanDefinitionNames() ) {
//                System.out.println(s);
//            }

        }


}
