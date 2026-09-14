package com.example.pvzstyle32;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.*;
import android.content.*;
import java.util.*;

public class MainActivity extends Activity {
    static { System.loadLibrary("garden_native"); }
    public native int nativeVersion();
    @Override public void onCreate(Bundle b){ super.onCreate(b); getWindow().setFlags(1024,1024); setContentView(new GameView(this)); }

    static class Plant { String name; int cost; int color; Plant(String n,int c,int col){name=n;cost=c;color=col;} }
    static class Zombie { float x,y,hp,max; Zombie(float x,float y){this.x=x;this.y=y;max=100;hp=max;} }
    static class Shot { float x,y; Shot(float x,float y){this.x=x;this.y=y;} }

    class GameView extends View {
        Paint p=new Paint(3); Paint text=new Paint(3); ArrayList<Zombie> zs=new ArrayList<>(); ArrayList<Shot> shots=new ArrayList<>();
        Plant[] plants=new Plant[50]; int selected=0,sun=250,wave=1; long last;
        GameView(Context c){super(c); text.setTypeface(Typeface.DEFAULT_BOLD); for(int i=0;i<50;i++) plants[i]=new Plant("Plant "+(i+1),50+(i%6)*25,Color.rgb(50+(i*37)%180,160+(i*13)%90,60+(i*17)%150)); last=System.currentTimeMillis();}
        protected void onDraw(Canvas c){ super.onDraw(c); int w=getWidth(),h=getHeight();
            p.setColor(Color.rgb(35,110,55)); c.drawRect(0,0,w,h,p); p.setColor(Color.rgb(210,225,165)); c.drawRect(80,80,w-30,h-30,p);
            int laneH=(h-100)/5; p.setColor(Color.rgb(175,195,130)); for(int r=0;r<5;r++){ if(r%2==0)c.drawRect(80,80+r*laneH,w-30,80+(r+1)*laneH,p); }
            p.setColor(Color.DKGRAY); for(int r=0;r<=5;r++) c.drawRect(80,80+r*laneH,w-30,82+r*laneH,p);
            p.setColor(Color.rgb(55,90,55)); c.drawRect(0,0,w,70,p); text.setTextSize(24); text.setColor(Color.WHITE); c.drawText("☀ "+sun,20,45,text); c.drawText("WAVE "+wave, w-150,45,text);
            int cardW=Math.max(70,(w-100)/6); for(int i=0;i<6;i++){int idx=(selected+i)%50; int x=20+i*cardW; p.setColor(plants[idx].color); c.drawRoundRect(x,75,x+cardW-8,145,12,12,p); text.setTextSize(14); text.setColor(Color.BLACK); c.drawText("P"+(idx+1),x+12,105,text); c.drawText("$"+plants[idx].cost,x+12,130,text);}
            for(int i=0;i<shots.size();i++){Shot s=shots.get(i); p.setColor(Color.YELLOW); c.drawCircle(s.x,s.y,6,p);}
            for(Zombie z:zs){p.setColor(Color.rgb(100,120,100)); c.drawCircle(z.x,z.y,24,p); p.setColor(Color.RED); c.drawRect(z.x-25,z.y-34,z.x+25,z.y-28,p); p.setColor(Color.GREEN); c.drawRect(z.x-25,z.y-34,z.x-25+50*z.hp/z.max,z.y-28,p);}
            text.setTextSize(16); text.setColor(Color.DKGRAY); c.drawText("Tap a card, then a lane to plant",95,h-15,text);
            update(); invalidate(); }
        void update(){ long now=System.currentTimeMillis(); float dt=Math.min(.05f,(now-last)/1000f); last=now; int h=getHeight();
            for(Zombie z:zs) z.x-=35*dt; for(Shot s:shots) s.x+=220*dt;
            for(Shot s:shots) for(Zombie z:zs) if(Math.abs(s.x-z.x)<22&&Math.abs(s.y-z.y)<28){z.hp-=20;s.x=-100;}
            for(int i=zs.size()-1;i>=0;i--) if(zs.get(i).hp<=0){sun+=25;zs.remove(i);} for(int i=shots.size()-1;i>=0;i--) if(shots.get(i).x>getWidth())shots.remove(i);
            if(now%7000<50){int lane=(int)(Math.random()*5); zs.add(new Zombie(getWidth()-55,90+lane*((h-100)/5)+((h-100)/10))); wave=Math.min(99,wave+1);}
        }
        public boolean onTouchEvent(android.view.MotionEvent e){ if(e.getAction()!=1)return true; float x=e.getX(),y=e.getY(); int h=getHeight(),w=getWidth();
            if(y>=75&&y<=145){int cardW=Math.max(70,(w-100)/6); int i=(int)(x/cardW); if(i>=0&&i<6) selected=(selected+i)%50; return true;}
            if(x>=80&&y>=80&&y<h-30){int lane=(int)((y-80)/((h-100)/5)); if(lane>=0&&lane<5&&sun>=plants[selected].cost){sun-=plants[selected].cost; shots.add(new Shot(120,90+lane*((h-100)/5)+((h-100)/10)));} }
            return true; }
    }
}
