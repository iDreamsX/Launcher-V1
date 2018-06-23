package fr.dreams.eorzialauncher;

import java.io.File;
import java.util.Arrays;

import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.litarvan.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.minecraft.MinecraftLauncher;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.ProcessLogManager;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.swinger.Swinger;


public class Launcher {

	public static final GameVersion EZ_VERSION = new GameVersion("1.7.10", GameType.V1_7_10);
	public static final GameInfos EZ_INFOS = new GameInfos("Eorzia", EZ_VERSION, new GameTweak[] {GameTweak.OPTIFINE});
	public static final File EZ_DIR = EZ_INFOS.getGameDir();
	public static final File EZ_CRASHES_FOLDER = new File(EZ_DIR, "crashes.txt");
	
	private static AuthInfos authInfos;
	private static Thread updateThread;
	
	private static CrashReporter crashReporter = new CrashReporter(null, EZ_CRASHES_FOLDER);

	public static void auth(String username, String password) throws AuthenticationException {
		Authenticator authenticator = new Authenticator("http://votresite.fr/votrerepertoire/server", AuthPoints.NORMAL_AUTH_POINTS);
		AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, username, password, "");
		authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());
	}
/*	public static void auth(String username , String password) throws AuthenticationException {
*		authInfos = new AuthInfos(username, "sry", "nope");
*	   }
*/
	
	public static void update() throws Exception {
		SUpdate su = new SUpdate("http://eorzialauncher.livehost.fr/", EZ_DIR);
		su.addApplication(new FileDeleter());
		
		updateThread = new Thread() {
			private int val;
			private int max;
			
			@Override
			public void run() {
				while(!this.isInterrupted()) {
					if(BarAPI.getNumberOfFileToDownload() ==0) {
						LauncherFrame.getInstance().getLauncherPanel().setInfoText("<html><b><font color = #ffffff >Verifications des fichiers</font></b></html>");
						continue;
					}
					val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
					max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);
					
					LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setMaximum(max);
					LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setValue(val);
					
					LauncherFrame.getInstance().getLauncherPanel().setInfoText("<html><b><font color = #ffffff >Telechargement des fichiers " +
							BarAPI.getNumberOfDownloadedFiles() + " / " + BarAPI.getNumberOfFileToDownload() + " " +
								Swinger.percentage(val, max) + "%</font></b></html>");
				}
			}
		};
		updateThread.start();
		su.start();
		updateThread.interrupt();
	}
	
	public static void launch() throws LaunchException {
		
		ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(EZ_INFOS, GameFolder.BASIC, authInfos);
		ExternalLauncher launcher = new ExternalLauncher(profile);
		profile.getVmArgs().addAll(Arrays.asList(LauncherFrame.getInstance().getLauncherPanel().getRamSelector().getRamArguments()));
		
		Process p = launcher.launch();
		
		ProcessLogManager manager = new ProcessLogManager(p.getInputStream(), new File(EZ_DIR, "logs.txt"));
		manager.start();
	
		try {
			Thread.sleep(5000L);
			LauncherFrame.getInstance().setVisible(false);
			p.waitFor();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	public static void interruptThread() {
		updateThread.interrupt();
	}
	
	public static CrashReporter getCrashReporter() {
		return crashReporter;
	}


	
}
