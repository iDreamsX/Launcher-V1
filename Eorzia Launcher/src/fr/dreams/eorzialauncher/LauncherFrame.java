package fr.dreams.eorzialauncher;

import javax.swing.JFrame;

import fr.theshark34.openlauncherlib.LanguageManager;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;

@SuppressWarnings("serial")
public class LauncherFrame extends JFrame{
	
	private static LauncherFrame instance;
	private LauncherPanel launcherPanel;
	private static CrashReporter crashReporter; 
	
	
	public LauncherFrame() {
		this.setTitle("Eorzia Launcher");
		this.setSize(1156, 650);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setIconImage(Swinger.getResource("icon.png"));
		this.setContentPane(launcherPanel = new LauncherPanel());
		
		
		WindowMover mover = new WindowMover(this);
		this.addMouseListener(mover);
		this.addMouseMotionListener(mover);
		
		this.setVisible(true);
	}

	public static void main(String[] args) {
		
		LanguageManager.setLang(LanguageManager.FRENCH);
		
		Swinger.setSystemLookNFeel();
		Swinger.setResourcePath("/fr/dreams/eorzialauncher/ressources/");
		Launcher.EZ_CRASHES_FOLDER.mkdirs();
		crashReporter = new CrashReporter("Eorzia Launcher", Launcher.EZ_CRASHES_FOLDER);
		
		instance = new LauncherFrame();
	}
	
	public static LauncherFrame getInstance() {
		return instance;
	}
	
	public static CrashReporter getCrashReporter() {
		return crashReporter;
	}

	public LauncherPanel getLauncherPanel(){
		return this.launcherPanel;
	}

}
