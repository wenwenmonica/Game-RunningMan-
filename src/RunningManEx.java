import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 * Created by Wenwen Yang on 6/5/15.
 */
public class RunningManEx extends JFrame {

    public RunningManEx() {

        initUI();
    }

    private void initUI() {

        add(new Board());

        setResizable(false);
        pack();

        setTitle("RunningMan");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                RunningManEx ex = new RunningManEx();
                ex.setVisible(true);
            }
        });
    }
}