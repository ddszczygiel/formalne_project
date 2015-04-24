package com.agh.met_for_project.app;


import com.agh.met_for_project.error.InvalidOperationException;
import com.agh.met_for_project.network.NetworkAnalyzer;
import com.agh.met_for_project.util.NetworkLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.agh.met_for_project")
public class App {

        public static void main(String[] args) throws InvalidOperationException {

            ConfigurableApplicationContext applicationContext = SpringApplication.run(App.class, args);
//            for ( String s : applicationContext.getBeanDefinitionNames() ) {
//                System.out.println(s);
//            }

            NetworkLoader networkLoader = (NetworkLoader) applicationContext.getBean("networkLoader");
            NetworkAnalyzer networkAnalyzer = (NetworkAnalyzer) applicationContext.getBean("networkAnalyzer");
            networkLoader.loadNetwork(null);
            networkAnalyzer.generate();
            networkAnalyzer.printTree();

        }

}
