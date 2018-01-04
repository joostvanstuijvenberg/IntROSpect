package nl.joostvanstuijvenberg.introspect.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost van Stuijvenberg on 07/10/2017.
 *
 * This file is part of IntROSpect. IntROSpect is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * IntROSpect is distributed in the hope that it will be useful but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with IntROSpect.  If
 * not, see <http://www.gnu.org/licenses/>.
 */

public class Graph {

    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Topic> topics = new ArrayList<>();
    private ArrayList<Service> services = new ArrayList<>();

    public Graph(Object[] systemState) {

        // Topics and their publishers.
        Object[] publishersInfo = (Object[]) systemState[0];
        for (Object publisherInfo : publishersInfo) {
            Object[] publishersContents = (Object[]) publisherInfo;
            Topic topic = createOrUpdateTopic((String) publishersContents[0]);
            Object[] publishers = (Object[]) publishersContents[1];
            for (Object publisher : publishers) {
                Node node = createOrUpdateNode((String) publisher);
                node.publishing.add(topic);
                topic.publishers.add(node);
            }
        }

        // Topics and their subscribers.
        Object[] subscribersInfo = (Object[]) systemState[1];
        for (Object subscriberInfo : subscribersInfo) {
            Object[] subscriberContents = (Object[]) subscriberInfo;
            Topic topic = createOrUpdateTopic((String) subscriberContents[0]);
            Object[] subscribers = (Object[]) subscriberContents[1];
            for (Object subscriber : subscribers) {
                Node node = createOrUpdateNode((String) subscriber);
                node.subscribed.add(topic);
                topic.subscribers.add(node);

            }
        }

        // Services and the nodes that provide them.
        Object[] servicesInfo = (Object[]) systemState[2];
        for (Object serviceInfo : servicesInfo) {
            Object[] servicesContents = (Object[]) serviceInfo;
            Service service = createOrUpdateService((String) servicesContents[0]);
            Object[] providers = (Object[]) servicesContents[1];
            for (Object provider : providers) {
                Node node = createOrUpdateNode((String) provider);
                node.providing.add(service);
                service.provider = node;
            }
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }

    private Node createOrUpdateNode(String name) {
        for (Node n : this.nodes)
            if (n.name.equals(name))
                return n;
        Node n = new Node();
        n.name = name;
        this.nodes.add(n);
        return n;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    private Topic createOrUpdateTopic(String name) {
        for (Topic t : this.topics)
            if (t.name.equals(name))
                return t;
        Topic t = new Topic();
        t.name = name;
        this.topics.add(t);
        return t;
    }

    public List<Service> getServices() {
        return services;
    }

    private Service createOrUpdateService(String name) {
        for (Service s : this.services)
            if (s.name.equals(name))
                return s;
        Service s = new Service();
        s.name = name;
        this.services.add(s);
        return s;
    }

    /**
     * Return a String representation of this Graph object, implemented as a large enumeration of
     * three views: a) nodes, the topics they publish and/or have subscribed to and the services
     * they provide, b) topics and the nodes that publish and/or have subscribed to them, and
     * c) all available services with their providing nodes.
     *
     * @return String representation of this Graph object.
     */
    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();

        // Nodes.
        for (Node n : nodes) {
            b.append("Node " + n.name);
            if (!n.publishing.isEmpty()) {
                b.append(" publishing to ");
                for (Topic t : n.publishing)
                    b.append(t.name + ", ");
            }
            if (!n.subscribed.isEmpty()) {
                b.append(" subscribed to ");
                for (Topic t : n.subscribed)
                    b.append(t.name + ", ");
            }
            if (!n.providing.isEmpty()) {
                b.append(" providing ");
                for (Service s : n.providing)
                    b.append(s.name + ", ");
            }
            b.append('\n');
        }
        b.append('\n');

        // Topics.
        for (Topic t : topics) {
            b.append("Topic " + t.name);
            if (!t.publishers.isEmpty()) {
                b.append(" published to by ");
                for (Node n : t.publishers)
                    b.append(n.name + ", ");
            }
            if (!t.subscribers.isEmpty()) {
                b.append(" subscribed to by ");
                for (Node n : t.subscribers)
                    b.append(n.name + ", ");
            }
            b.append('\n');
        }
        b.append('\n');

        // Services.
        for (Service s : services) {
            b.append("Service " + s.name);
            b.append(" provided by " + s.provider.name);
            b.append('\n');
        }
        b.append('\n');

        return b.toString();
    }

}
