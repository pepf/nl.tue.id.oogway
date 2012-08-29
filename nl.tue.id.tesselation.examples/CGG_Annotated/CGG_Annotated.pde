import processing.pdf.*;
import nl.tue.id.oogway.*;

int XSIZE=int(3.6*297);
int YSIZE=int(3.6*210);

Oogway o;
float distance_MB;
float distance_CA;

PFont font;

void setup() {
  size(XSIZE, YSIZE);
  o = new Oogway(this);
  noLoop();
  beginRecord(PDF, "CGG_Annotated.pdf");
  o.setPenColor(0);
  font = createFont("Comic Sans MS",32); 
}

void draw() {
  background(255);
  drawIntro();
  
  tesselate(100);

  o.home();
  o.setPosition(250, o.ycor()+250);
  abc(250);
  drawIHM();
  textABC();

  endRecord();
}

void tesselate(float size) {
  o.home();
  calc_CA_MB(100);
  o.setPosition(o.xcor()+ distance_MB, o.ycor()+50);
  float x = o.xcor();
  float y = o.ycor();

  for (int i=0; i<2; i++) {

    o.setPosition(x + distance_MB, y + distance_CA * i);  
    for (int j=0; j<2; j++) {
      abcRotated(100);
      abc(100);
      o.setPosition(o.xcor()+distance_MB*2, o.ycor());
    }

    o.setPosition(x, o.ycor() + distance_CA/2);
    for (int j=0; j<2; j++) {
      abcReflected(100);
      abcReflectedRotated(100);
      o.setPosition(o.xcor()+distance_MB*2, o.ycor());
    }
  }
}

void abc(float size) {
  // we assume Oogway starts at A, heading right.
  o.pushState();

  float ax = o.xcor();
  float ay = o.ycor();

  o.left(45);
  o.beginPath("AB.svg"); 
  o.forward(size); 
  o.endPath();

  float bx = o.xcor();
  float by = o.ycor();

  o.left(90);
  o.beginReflection();
  o.beginPath("AB.svg"); 
  o.forward(size); 
  o.endPath();
  o.endReflection();

  distance_CA = o.distance(ax, ay);
  o.setHeading(o.towards(ax, ay));
  o.beginPath("CM.svg"); 
  o.forward(distance_CA/2); 
  o.endPath();
  float mx = o.xcor();
  float my = o.ycor();

  distance_MB = o.distance(bx, by);

  o.setPosition(ax, ay);
  o.setHeading(o.towards(mx, my));
  o.beginPath("CM.svg"); 
  o.forward(distance_CA/2); 
  o.endPath();

  o.popState();
  
  drawArrow(size);
}

void abcReflected(float size) { //reflect against AC
  // we assume Oogway starts at A, heading right.
  o.pushState();
  o.left(180);
  o.beginReflection();
  abc(size);
  o.endReflection();
  o.popState();
}

void abcReflectedRotated(float size) {
  // we assume Oogway starts at A, heading right.
  o.pushState();
  o.left(180);
  o.beginReflection();
  abcRotated(size);
  o.endReflection();
  o.popState();
}

void abcRotated(float size) {
  o.pushState();
  o.setPosition(o.xcor(), o.ycor()-distance_CA);
  o.left(180);
  abc(size);
  o.popState();
}

void calc_CA_MB(float size) {
  o.pushState();
  o.penup();
  abc(size);
  o.popState();
}

void drawIHM(){
  pushStyle();
  o.pushState();
  textFont(font,16);
  fill(0,0,255);
  o.setPenColor(0,0,255);
  o.setPosition(o.xcor()+distance_MB/2, o.ycor());
  ellipse(o.xcor(), o.ycor(), 10 , 10);
  text("I", o.xcor()+10, o.ycor());
  o.left(90);
  
  o.beginDash();
  o.forward(distance_CA);
  ellipse(o.xcor(), o.ycor(), 10 , 10);
  text("H", o.xcor()+10, o.ycor());  
  o.endDash();
  
  o.setPosition(o.xcor()-distance_MB/2, o.ycor()+distance_CA/2);
  ellipse(o.xcor(), o.ycor(), 10 , 10);
  text("M", o.xcor()+10, o.ycor());  

  o.popState(); 
  popStyle(); 
}

void textABC(){
  pushStyle();
  o.pushState();
  textFont(font,16);
  fill(255,0,0);
  ellipse(o.xcor(), o.ycor(), 10 , 10);
  text("A", o.xcor()+10, o.ycor());
  
  o.setPosition(o.xcor(), o.ycor()-distance_CA);  
  ellipse(o.xcor(), o.ycor(), 10 , 10);
  text("C", o.xcor()+10, o.ycor());  

  o.setPosition(o.xcor() + distance_MB, o.ycor()+distance_CA/2);  
  ellipse(o.xcor(), o.ycor(), 10 , 10);
  text("B", o.xcor()+10, o.ycor());   
  
  o.popState(); 
  popStyle();
}

void drawArrow(float size){
  if(!o.isDown()) return;
  
  o.pushState();

  if(o.isReflecting()){
    o.setPenColor(0,0,255);
    fill(0,0,255);
  }
  else{
    o.setPenColor(255,0,0);
    fill(255,0,0);
  }

  o.penup();
  o.left(90);
  o.forward(distance_CA/2);
  o.right(90);
  o.forward(.6*distance_MB);
  o.left(180);
  o.pendown();
  o.forward(.2*distance_MB);
  o.setStamp(o.OARROWRIGHT);
  o.stamp(size/16);

  o.popState(); 
}

void drawIntro(){
  pushStyle();
  fill(0);
     textFont(font,32);
     text("Nr.21, Basic Type CGG",200,50);
     textFont(font,16);
     text("Bring the arbitrary line AB by glide reflection until it connects to the position BC, where the "
     +"angle ABC is arbitrary (axis of glide reflection IH parallel to AC at equal distance from A "
     +" and B). Close the figure by a C-line CA."
         , 200, 100,700,200); 
     text("Network 666,\r\n"
     +"4 positionings."
         , 650, 200,700,100); 
 popStyle();
}
  

