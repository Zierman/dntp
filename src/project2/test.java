package project2;

public class test {
    public static void main(String[] args) {
        System.out.println("test main running");

        ChunkFrameReceiver receiver = new ChunkFrameReceiver();
        ChunkFrameSender sender = new ChunkFrameSender();



        System.out.println("end test");
    }
}