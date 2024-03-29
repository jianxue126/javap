package com.jx.mqtt;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/*服务器端向客户端推送不同的主题*/
public class Server {
    // public static final String SERVER_URL = "tcp://0.0.0.0:61613";
    public static final String SERVER_URL = "tcp://192.168.127.129:1883";
    public static List<String> TOPICList = Arrays.asList("wether", "car", "topic");
    public static String clientid;

    private MqttClient mqttClient;
    public MqttTopic mqttTopic1;
    private MqttTopic getMqttTopic2;
    private String username = "admin";
    private String password = "password";

    private MqttMessage mqttMessage;

    /*
     * //创建新的topic链接的时候使用此构造方法 public Server() throws Exception{ mqttClient = new
     * MqttClient(SERVER_URL, clientid, new MemoryPersistence()); Server server =
     * new Server(); server.mqttMessage = new MqttMessage();
     * server.mqttMessage.setQos(2); server.mqttMessage.setRetained(true);
     * server.mqttMessage.setPayload("装配成功".getBytes());
     * server.publish(server.mqttTopic1,server.mqttMessage);
     * System.out.println(server.mqttMessage.isRetained()+":retained状态"); connect();
     * }
     */

    public Server() throws Exception {
        clientid = "wether";
        mqttClient = new MqttClient(SERVER_URL, clientid, new MemoryPersistence());
        String topic = "wether";
        connect(topic);
    }

    public Server(String topic, String clientid) throws Exception {
        TOPICList.add(topic);
        mqttClient = new MqttClient(SERVER_URL, clientid, new MemoryPersistence());
        this.createCon(topic);
        // connect(topic);

    }

    public void connect(String topic) {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        mqttConnectOptions.setConnectionTimeout(20);
        mqttConnectOptions.setKeepAliveInterval(20);

        try {
            mqttClient.setCallback(new PushCallback());
            mqttClient.connect(mqttConnectOptions);
            mqttTopic1 = mqttClient.getTopic(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void publish(MqttTopic topic, MqttMessage message) throws MqttException {
        System.out.println("话题是" + topic.toString() + "要发送的消息是" + message.toString());
        MqttDeliveryToken publish_token = topic.publish(message);
        publish_token.waitForCompletion();
        System.out.print("消息已经推送到客户端了");
    }

    public static void main(String[] args) throws Exception {

        Server server = new Server();
        server.mqttMessage = new MqttMessage();
        server.mqttMessage.setQos(2);
        server.mqttMessage.setRetained(true);

        int i = 0;
        for (i = 0; i < 50; i++) {
            String payLoad = "消息组件" + UUID.randomUUID().toString();
            server.mqttMessage.setPayload(payLoad.getBytes());
            server.publish(server.mqttTopic1, server.mqttMessage);
            Thread.sleep(4000);
        }
//		server.mqttMessage.setPayload("装配成功".getBytes());
//		server.publish(server.mqttTopic1, server.mqttMessage);
        System.out.println(server.mqttMessage.isRetained() + ":retained状态");
    }

    public String createCon(String topic) throws Exception {
        Server server = new Server();
        server.mqttMessage = new MqttMessage();
        server.mqttMessage.setQos(2);
        server.mqttMessage.setRetained(true);
        server.mqttMessage.setPayload("装配成功".getBytes());
        server.publish(server.getMqttClient().getTopic(topic), server.mqttMessage);
        System.out.println(server.mqttMessage.isRetained() + ":retained状态");
        return "build success";
    }

    public static String getServerUrl() {
        return SERVER_URL;
    }

    public List<String> getTOPIC() {
        return TOPICList;
    }

    public static String getClientid() {
        return clientid;
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

    public MqttTopic getMqttTopic1() {
        return mqttTopic1;
    }

    public MqttTopic getGetMqttTopic2() {
        return getMqttTopic2;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public MqttMessage getMqttMessage() {
        return mqttMessage;
    }
}