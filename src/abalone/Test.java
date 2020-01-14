package abalone;


public class Test {

	private static Board board;
	
    public static void setUp() {
    	board = new Board();
    }

    public static void testSelection() {
    	//First they should all be occupied.
    	System.out.println(board.isValidSelection(0, 0, 2, 2) == true);
        System.out.println(board.isValidSelection(0, 0, 3, 3) == false);
        System.out.println(board.isValidSelection(0, 1, 1, 0) == false);
        System.out.println(board.isValidSelection(0, 0, 1, 0) == true);
    }
    
    public static void main(String[] args) {
    	setUp();
    	testSelection();
    }

}
