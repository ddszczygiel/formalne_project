package com.agh.met_for_project.app;


import com.agh.met_for_project.error.InvalidOperationException;
import com.agh.met_for_project.network.CoverTree;
import com.agh.met_for_project.network.NetworkAnalyzer;
import com.agh.met_for_project.network.ReachTree;
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
//            NetworkAnalyzer networkAnalyzer = (NetworkAnalyzer) applicationContext.getBean("networkAnalyzer");
            CoverTree coverTree = (CoverTree) applicationContext.getBean("coverTree");
            ReachTree reachTree = (ReachTree) applicationContext.getBean("reachTree");
            networkLoader.loadNetwork(null);
//            coverTree.buildCoverTree();
//            coverTree.displayTree();
//            reachTree.buildReachTree();
//            reachTree.displayTree();
            NetworkAnalyzer analyzer = (NetworkAnalyzer) applicationContext.getBean("networkAnalyzer");
            System.out.println(analyzer.getMatrixModel().getMatrix());

        }

}
