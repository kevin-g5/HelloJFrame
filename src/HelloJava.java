

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import com.sun.jna.Platform;

// NOTE: java.awt.Robot can't properly capture transparent pixels

public class HelloJava {

    MouseInputAdapter handler = new MouseInputAdapter() {
        private Point offset;
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e))
                offset = e.getPoint();
        }
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                System.exit(1);
            }
        }
        public void mouseReleased(MouseEvent e) {
            offset = null;
        }
        public void mouseDragged(MouseEvent e) {
            if (offset != null) {
                Window w = (Window)e.getSource();
                Point where = e.getPoint();
                where.translate(-offset.x, -offset.y);
                Point loc = w.getLocationOnScreen();
                loc.translate(where.x, where.y);
                w.setLocation(loc.x, loc.y);
            }
        }
    };

    private Robot robot;

    protected void setUp() throws Exception {
        if (!GraphicsEnvironment.isHeadless())
            robot = new Robot();
    }

    protected void tearDown() {
        robot = null;
        if (!GraphicsEnvironment.isHeadless()) {
            Window[] owned = JOptionPane.getRootFrame().getOwnedWindows();
            for (int i=0;i < owned.length;i++) {
                owned[i].dispose();
            }
        }
    }

    private static final int X = 100;
    private static final int Y = 100;
    private static final int W = 100;
    private static final int H = 100;

    /**
     * Verfies that the specified pixel within the image has the expected color component values.
     *
     * @param img The image to be checked.
     * @param x The X coordinate of the pixel to be checked.
     * @param y The Y coordinate of the pixel to be checked.
     * @param expectedRed The expected value of the red color component.
     * @param expectedGreen The expected value of the green color component.
     * @param expectedBlue The expected value of the blue color component.
     */
    public static void assertPixelColor(final BufferedImage img, final int x, final int y, final int expectedRed, final int expectedGreen, final int expectedBlue){
        int rgb = img.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb & 0xFF);

        assertEquals(expectedRed, r);
        assertEquals(expectedGreen, g);
        assertEquals(expectedBlue, b);
    }

    /**
     * Extracts the values of the color components at the specified pixel.
     *
     * @param img The concerning image.
     * @param x The X coordinate of the concerning pixel.
     * @param y The Y coordinate of the concerning pixel.
     * @return An array with three elements that represents the color components of the pixel: Red, green, blue.
     */
    public static int[] getPixelColor(final BufferedImage img, final int x, final int y){
        int rgb = img.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb & 0xFF);

        return new int[]{r,g,b};
    }

    public void noteReveal() throws Exception {
        final int SIZE = 200;
        System.setProperty("sun.java2d.noddraw", "true");
        GraphicsConfiguration gconfig =
            WindowUtils.getAlphaCompatibleGraphicsConfiguration();
        Window w;
        Container content;
        if (true) {
            JFrame frame = new JFrame(getName(), gconfig);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            content = frame.getContentPane();
            w = frame;
        } else {
            Frame frame = JOptionPane.getRootFrame();
            JWindow window = new JWindow(frame, gconfig);
            content = window.getContentPane();
            w = window;
        }
        final Window f = w;
        WindowUtils.setWindowTransparent(f, true);
        content.add(new JButton("Hello World!") {
            private static final long serialVersionUID = 1L;

            {
                addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
            }
        }, BorderLayout.SOUTH);
        content.add(new JComponent() {
            private static final long serialVersionUID = 1L;

            public Dimension getPreferredSize() {
                return new Dimension(SIZE, SIZE);
            }
            protected void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D)graphics.create();
                g.setComposite(AlphaComposite.Clear);
                g.fillRect(0,0,SIZE,SIZE);
                g.dispose();

                g = (Graphics2D)graphics.create();
                Color[] colors = {
                    new Color(0,0,0),
                    new Color(0,0,0,128),
                    new Color(128,128,128),
                    new Color(128,128,128,128),
                    new Color(255,255,255),
                    new Color(255,255,255,128),
                };
                for (int i=0;i < colors.length;i++) {
                    g.setColor(colors[i]);
                    g.fillRect((SIZE * i)/colors.length, 0,
                               (SIZE + colors.length-1)/colors.length, SIZE);
                }
                g.setColor(Color.red);
                g.drawRect(0, 0, SIZE-1, SIZE-1);
                g.dispose();
                SwingUtilities.getWindowAncestor(this).toFront();
            }
        });
        f.pack();
        f.addMouseListener(handler);
        f.addMouseMotionListener(handler);
        f.setLocation(100, 100);
        f.setVisible(true);
        while (f.isVisible()) {
            Thread.sleep(1000);
            //f.repaint();
        }
    }
 
    public static void main(String[] args) {
        noteReveal();
    }
}

