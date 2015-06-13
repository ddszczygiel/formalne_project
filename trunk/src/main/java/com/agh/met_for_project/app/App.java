package com.agh.met_for_project.app;


import com.agh.met_for_project.error.InvalidOperationException;
import com.agh.met_for_project.model.NetworkState;
import com.agh.met_for_project.network.CoverTree;
import com.agh.met_for_project.network.NetworkAnalyzer;
import com.agh.met_for_project.network.PetriesNetwork;
import com.agh.met_for_project.network.ReachTree;
import com.agh.met_for_project.util.NetworkLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@ComponentScan("com.agh.met_for_project")
public class App {

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("5120MB");
        factory.setMaxRequestSize("5120MB");
        return factory.createMultipartConfig();
    }

        public static void main(String[] args) throws InvalidOperationException {

            ConfigurableApplicationContext applicationContext = SpringApplication.run(App.class, args);
//            for ( String s : applicationContext.getBeanDefinitionNames() ) {
//                System.out.println(s);
//            }

            NetworkLoader networkLoader = (NetworkLoader) applicationContext.getBean("networkLoader");
            NetworkAnalyzer networkAnalyzer = (NetworkAnalyzer) applicationContext.getBean("networkAnalyzer");
            CoverTree coverTree = (CoverTree) applicationContext.getBean("coverTree");
            PetriesNetwork network = (PetriesNetwork) applicationContext.getBean("petriesNetwork");
            ReachTree reachTree = (ReachTree) applicationContext.getBean("reachTree");
            networkLoader.loadNetwork(null);
//            network.setPrioritySimulation(true);
            coverTree.buildCoverTree();
//            for (NetworkState state : coverTree.getStates()) {
//                System.out.println(state.getState());
//                System.out.println(state.getExecutedTransitions());
//                System.out.println(state.getPath());
//                System.out.println();
//            }
            System.out.println(networkAnalyzer.checkNetworkLivness());
            System.out.println(networkAnalyzer.isReversible());
            coverTree.displayTree();
//            reachTree.buildReachTree();
//            reachTree.displayTree();
//            NetworkAnalyzer analyzer = (NetworkAnalyzer) applicationContext.getBean("networkAnalyzer");
//            System.out.println(analyzer.getMatrixModel().getMatrix());

        }

}
