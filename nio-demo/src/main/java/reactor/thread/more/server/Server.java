package reactor.thread.more.server;

public class Server {

    public static void main(String[] args) {
        Reactor reactor = new Reactor(9100);
        reactor.run();
    }
}
