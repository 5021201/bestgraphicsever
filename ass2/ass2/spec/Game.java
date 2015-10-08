package ass2.spec;

import java.io.File;
import java.io.FileNotFoundException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;


/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener {

    private Terrain myTerrain;

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();
          panel.addGLEventListener(this);
 
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();

          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
    	Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		// do stuff with camera
		gl.glRotated(180, 0, 1, 0);
		gl.glTranslated(0, -3, 8);
		// draw the terrain
		drawTerrain(gl);
		
		
	}
	
	
	
	private void drawTerrain(GL2 gl) {
		int width = (int)myTerrain.size().getWidth();
		int height = (int)myTerrain.size().getHeight();
		gl.glColor4d(1, 1, 1, 1);

		double altitude;
		gl.glBegin(GL2.GL_TRIANGLES); {
		    for (int z = height - 1; z > 0; z--) {
				for (int x = width - 1; x > 0; x--) {
					// draw first triangle
					altitude = myTerrain.getGridAltitude(x, z);
					gl.glVertex3d(x, altitude, z);
					altitude = myTerrain.getGridAltitude(x-1,z);
					gl.glVertex3d(x-1, altitude, z);
					altitude = myTerrain.getGridAltitude(x-1,z-1);
					gl.glVertex3d(x-1, altitude, z-1);
					
					// draw second triangle
					altitude = myTerrain.getGridAltitude(x,z);
					gl.glVertex3d(x, altitude, z);
					altitude = myTerrain.getGridAltitude(x-1,z-1);
					gl.glVertex3d(x-1, altitude, z-1);
					altitude = myTerrain.getGridAltitude(x,z-1);
					gl.glVertex3d(x, altitude, z-1);
					

				}
			}
		}
		gl.glEnd();
		for(Tree t : myTerrain.trees()) {
        	t.draw(gl);
        }
	}
	

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();	
    	//gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();
		glu.gluPerspective(60,1,1,100);
		
	}
	
    @Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		 switch (e.getKeyCode()) {  
		 	case KeyEvent.VK_UP:
            
		 	case KeyEvent.VK_DOWN:
			     
				  angle = (angle - 10) % 360;
				  break;		
		 default:
			 break;
		 }
		 System.out.println(angle);
    }
}
