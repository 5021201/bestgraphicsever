package ass2.spec; 

import java.awt.Dimension;

public class Camera {
     private double currentX;
     private double currentY;
     private double currentZ;
	   private double currentRotation;
     
    public Camera(Terrain myTerrain) {
    	 Dimension terrainSize = myTerrain.size();
		   currentX = terrainSize.getWidth()/2;
    	 currentY = 0.5;
    	 currentZ = terrainSize.getHeight();
    	 currentRotation = 0;
     }
	 
	 public double getCurrentX() {
		 return currentX;
	 }
	 
	 public double getCurrentY() {
		 return currentY;
	 }
	 
	 public double getCurrentZ() {
		 return currentZ;
	 }
	 
	 public double getCurrentRotation() {
		 return currentRotation;
	 }
	 
	 public void changeCurrentRotation(double newRotate) {
	     currentRotation += newRotate;	 
	 }
	 
	 public void changeCurrentX(double newX) {
		 currentX += newX;		 
	 }
	 
	 public void changeCurrentZ(double newZ) {
		 currentZ += newZ;
	 }
	
}


