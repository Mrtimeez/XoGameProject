import java.util.Scanner;

public class OXgame {

    static final char EMPTY = ' ';
    static char[] board = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };

    public static void main(String[] args) {
        System.out.println(" ยินดีต้อนรับเข้าสู่เกม XO ");
        ExamBoard();
        while (true) {
            playerMove();
            if (GameEnd('O')) break;
            displayBoard();
            computerMove();
            if (GameEnd('X')) break;
            displayBoard();
        }
    }
    //แสดงตำแหน่งที่ถูกเลือกไปแล้ว
    public static void displayBoard() {
        System.out.println("| - - - - - | ");
        System.out.println("| " + board[0] + " | " + board[1] + " | " + board[2] + " |");
        System.out.println("| - + - + - | ");
        System.out.println("| " + board[3] + " | " + board[4] + " | " + board[5] + " |");
        System.out.println("| - + - + - | ");
        System.out.println("| " + board[6] + " | " + board[7] + " | " + board[8] + " |");
        System.out.println("| - - - - - | ");
    }

    //แสดงตัวอย่างให้ผู้เล่นเลือกตำแหน่งที่จะวางเครื่องหมาย X หรือ O
    public static void ExamBoard() {
        System.out.println("| - - - - - | ");
        System.out.println("| " + "1" + " | " + "2" + " | " + "3" + " |");
        System.out.println("| - + - + - | ");
        System.out.println("| " + "4" + " | " + "5" + " | " + "6" + " |");
        System.out.println("| - + - + - | ");
        System.out.println("| " + "7" + " | " + "8" + " | " + "9" + " |");
        System.out.println("| - - - - - | ");
    }

    //ผู้เล่นเลือกตำแหน่งที่จะวางเครื่องหมาย
    public static void playerMove() {
        Scanner scanner = new Scanner(System.in);
        int move;
        while (true) {
            System.out.print("ใส่ตำแหน่งของคุณ (1-9): ");
            if (scanner.hasNextInt()) {
                move = scanner.nextInt()-1;
                if (move >= 0 && move < 9 && board[move] == ' ') {
                    board[move] = 'O';
                    break;
                } else {
                    System.out.println("ตำแหน่งไม่ถูกต้อง กรุณาลองใหม่");
                }
            } else {
                System.out.println("กรุณาใส่เลข");
                scanner.next();
            }
        }
        
    }

    public static void computerMove() {
        int bestMove = findBestMove();
        board[bestMove] = 'X'; // คอมพิวเตอร์เป็น 'X'
        System.out.println("Computer chose position: " + (bestMove + 1));
    }
    
    public static int findBestMove() {
        int bestVal = Integer.MIN_VALUE;
        int bestMove = -1;
    
        // ลูปหาช่องว่าง
        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                // ทำการทดลองใส่ 'X'
                board[i] = 'X';
    
                // ประเมินค่าของการเคลื่อนไหวนี้โดยใช้ Minimax
                int moveVal = minimax(0, false);
    
                // ย้อนกลับการเคลื่อนไหว
                board[i] = ' ';
    
                // หากค่าของการเคลื่อนไหวนั้นดีที่สุดที่เคยเจอมา ให้บันทึกเป็นการเคลื่อนไหวที่ดีที่สุด
                if (moveVal > bestVal) {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        }
        return bestMove;
    }
    
    // ฟังก์ชั่น Minimax
    public static int minimax(int depth, boolean isMaximizing) {
        int score = evaluate(); // ประเมินผลลัพธ์ของบอร์ดปัจจุบัน
    
        // ถ้าคอมพิวเตอร์ชนะ
        if (score == 10) {
            return score - depth; // หักลบความลึกเพื่อให้การชนะเร็วที่สุดดีขึ้น
        }
    
        // ถ้าผู้เล่นชนะ
        if (score == -10) {
            return score + depth; // หักลบความลึกเพื่อให้การชนะช้าลง
        }
    
        // ถ้าเสมอกัน
        if (isBoardFull()) {
            return 0;
        }
    
        // ถ้าถึงตาของคอมพิวเตอร์ (Maximizer)
        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
    
            // ลูปหาช่องว่าง
            for (int i = 0; i < 9; i++) {
                if (board[i] == ' ') {
                    board[i] = 'X'; // สมมุติว่าคอมพิวเตอร์เลือกช่องนี้
    
                    // ใช้ Minimax เพื่อประเมินค่าของการเคลื่อนไหวนี้
                    best = Math.max(best, minimax(depth + 1, false));
    
                    board[i] = ' '; // ย้อนกลับ
                }
            }
            return best;
        } 
        // ถ้าถึงตาของผู้เล่น (Minimizer)
        else {
            int best = Integer.MAX_VALUE;
    
            // ลูปหาช่องว่าง
            for (int i = 0; i < 9; i++) {
                if (board[i] == ' ') {
                    board[i] = 'O'; // สมมุติว่าผู้เล่นเลือกช่องนี้
    
                    // ใช้ Minimax เพื่อประเมินค่าของการเคลื่อนไหวนี้
                    best = Math.min(best, minimax(depth + 1, true));
    
                    board[i] = ' '; // ย้อนกลับ
                }
            }
            return best;
        }
    }
    
    // ฟังก์ชั่นเพื่อประเมินบอร์ด
    public static int evaluate() {
        // ตรวจสอบว่าคอมพิวเตอร์หรือผู้เล่นชนะ
        if (checkWin('X')) {
            return 10; // คอมพิวเตอร์ชนะ
        } else if (checkWin('O')) {
            return -10; // ผู้เล่นชนะ
        }
        return 0; // ยังไม่มีใครชนะ
    }

    //เช็คว่ากระดานเต็มมั้ย
    public static boolean isBoardFull() {
        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean checkWin(char player) {
        // ตรวจสอบแถวแนวนอน แนวตั้ง และแนวทแยง
        for (int i = 0; i < 3; i++) {
            if ((board[i * 3] == player && board[i * 3 + 1] == player && board[i * 3 + 2] == player) ||
                    (board[i] == player && board[i + 3] == player && board[i + 6] == player)) {
                return true;
            }
        }
        if ((board[0] == player && board[4] == player && board[8] == player) ||
                (board[2] == player && board[4] == player && board[6] == player)) {
            return true;
        }
        return false;
    }
    //ส่งค่าเกมจบ
    static boolean GameEnd(char player) {
        if (checkWin(player)) {
            displayBoard();
            if (player == 'O') {
                System.out.println("คุณชนะ!!");
            } else {
                System.out.println("คอมพิวเตอร์ชนะ !!");
            }
            return true;
        }
        if (isBoardFull()) {
            displayBoard();
            System.out.println("เสมอ!");
            return true;
        }
        return false;
    }

}
