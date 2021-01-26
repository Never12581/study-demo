package reactor.thread.single.server;

public class Server {

    public static void main(String[] args) {
        Reactor reactor = new Reactor(9090);
        reactor.run();
    }
}
