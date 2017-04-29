package frontend;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;


//Use MediaPlayer, all of Applet is deprecated
public class SoundHandler {

	public void playSound(Boolean loop, String urlString) {
		if (urlString.substring(urlString.length()-4).equals(".wav")) {
			URL url = getClass().getClassLoader().getResource(urlString);
			AudioClip clip = Applet.newAudioClip(url);
			if (loop) {
				clip.loop();
			}
			else {
				clip.play();
			}
		}
		else {
			System.out.println("Audio file must be in .wav format");
		}
	}
}
