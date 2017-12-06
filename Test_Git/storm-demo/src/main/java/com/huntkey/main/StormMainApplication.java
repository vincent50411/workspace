package com.huntkey.main;

import com.huntkey.main.bolt.PrintLineBolt;
import com.huntkey.main.bolt.WordCounter;
import com.huntkey.main.bolt.WordNormalizer;
import com.huntkey.main.bolt.kafka.FilterBolt;
import com.huntkey.main.bolt.kafka.TransferBolt;
import com.huntkey.main.spout.WordReader;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by liuwens on 2017/9/5.
 */
public class StormMainApplication
{
    public static void main(String[] args) throws InterruptedException
    {
        kafkaClientTest(args);
    }


    public void stormLocalTest(String[] args) throws InterruptedException
    {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("word-reader", new WordReader());

        builder.setSpout("word-reader_2", new WordReader());

        builder.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader" );

        builder.setBolt("word-counter" , new WordCounter()).fieldsGrouping("word-normalizer" , new Fields("word"));

        builder.setBolt("print-line-message", new PrintLineBolt()).shuffleGrouping("word-reader_2" ).shuffleGrouping("word-normalizer" );

        StormTopology stormTopology = builder.createTopology();

        Config config = new Config();
        String fileName = "D:\\Test\\words.txt";//"/root/Documents/liuwens/words.txt";//"D:\\Test\\words.txt" ;
        config.put("fileName" , fileName );
        //config.setNumWorkers(2);
        config.setDebug(false);


        if(args != null && args.length > 0)
        {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("Getting-Started-Topologie" , config , stormTopology );

            Thread.sleep(5000);
            cluster.shutdown();
        }
        else
        {
            try {
                StormSubmitter.submitTopology("word-count-topology" , config, stormTopology );

                Thread. sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvalidTopologyException e) {
                e.printStackTrace();
            } catch (AlreadyAliveException e) {
                e.printStackTrace();
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        }


        //

    }

    private static void kafkaClientTest(String[] args) throws InterruptedException {
        String BROKER_LIST = "vm-linux-163.hkhf.hkgp.net:6667,vm-linux-164.hkhf.hkgp.net:6667,vm-linux-165.hkhf.hkgp.net:6667";
        final String TOPIC = "spark_streaming_kafka_test";

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "BROKER_LIST");
        String zkRoot = "/storm";
        String id = "word";


        BrokerHosts brokerHosts = new ZkHosts(BROKER_LIST);
        SpoutConfig spoutConf = new SpoutConfig(brokerHosts, TOPIC, zkRoot, id);
        spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConf.zkServers = Arrays.asList(BROKER_LIST.split(","));
        spoutConf.zkPort = 6667;

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka-reader", new KafkaSpout(spoutConf),1);
        builder.setBolt("filter-bolt",new FilterBolt(),1).shuffleGrouping("kafka-reader");
        builder.setBolt("input-line",new TransferBolt(),1).shuffleGrouping("reader-input");

        Config config = new Config();
        String name = "my_test_topology";
        config.setNumWorkers(4);

        if(args != null && args.length > 0)
        {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(name, config , builder.createTopology());

            Thread.sleep(5000);
            cluster.shutdown();
        }
        else
        {
            try
            {
                StormSubmitter.submitTopologyWithProgressBar(name, config, builder.createTopology());

                Thread. sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvalidTopologyException e) {
                e.printStackTrace();
            } catch (AlreadyAliveException e) {
                e.printStackTrace();
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        }

    }



}
