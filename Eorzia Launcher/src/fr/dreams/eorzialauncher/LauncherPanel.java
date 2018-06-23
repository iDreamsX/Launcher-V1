package fr.dreams.eorzialauncher;

import static fr.theshark34.swinger.Swinger.drawFullsizedImage;
import static fr.theshark34.swinger.Swinger.getResource;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;

import fr.litarvan.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener {
	
	private Image background = getResource("background.png");

	private Saver saver = new Saver(new File(Launcher.EZ_DIR, "launcher.properties"));
	
	private JTextField usernameField = new JTextField(saver.get("username"));
	private JTextField passwordField = new JPasswordField();
	private JEditorPane jep = new JEditorPane();
	
	private STexturedButton playButton = new STexturedButton(getResource("play2.png"));
	private STexturedButton quitButton = new STexturedButton(getResource("quit.png"));
	private STexturedButton hideButton = new STexturedButton(getResource("hide.png"));
	private STexturedButton ramButton = new STexturedButton(getResource("settings.png"));
	
    private STexturedButton teamspeakButton = new STexturedButton(getResource("teamspeak.png"));
    private STexturedButton discordButton = new STexturedButton(getResource("discord.png"));
    private STexturedButton twitterButton = new STexturedButton(getResource("twitter.png"));
    private STexturedButton webButton = new STexturedButton(getResource("web.png"));
    
    private STexturedButton problButton = new STexturedButton(getResource("probl_connexion.png"));
    private STexturedButton createaccountButton = new STexturedButton(getResource("create_account.png"));



	private SColoredBar progressBar = new SColoredBar(new Color(255, 255, 255, 200), new Color(102, 187, 106, 175));
	private JLabel infoLabel = new JLabel("<html><b><font color = #ffffff >Clique sur connexion pour débuter ! </font></b></html>", SwingConstants.LEFT);

	
    private RamSelector ramSelector = new RamSelector(new File(Launcher.EZ_DIR, "ram.txt"));
	
	
	public LauncherPanel() {
		this.setLayout(null);
		
		this.usernameField.setFont(usernameField.getFont().deriveFont(15F));
		this.usernameField.setOpaque(false);
		this.usernameField.setBorder(null);
		this.usernameField.setBounds(80, 258, 325, 42);
		add(usernameField);

		this.passwordField.setFont(passwordField.getFont().deriveFont(15F));
		this.passwordField.setOpaque(false);
		this.passwordField.setBorder(null);
		this.passwordField.setBounds(80, 350, 325, 42);
		add(passwordField);
		
		this.playButton.setBounds(78, 372);
		this.playButton.addEventListener(this);
		add(playButton);
		
		this.quitButton.setBounds(1117, 6);
		this.quitButton.addEventListener(this);
		add(quitButton);
		
		this.hideButton.setBounds(1080, 6);
		this.hideButton.addEventListener(this);
		add(hideButton);
		
		this.progressBar.setStringPainted(true);
		this.progressBar.setBounds(0, 640, 1156, 10);
		add(progressBar);
		
		this.infoLabel.setFont(infoLabel.getFont().deriveFont(15F));
		this.infoLabel.setFont(new Font("Roboto", Font.PLAIN, 15));
		this.infoLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.infoLabel.setBounds(50, 620, 1156, 15);
		add(infoLabel);
		
		this.ramButton.addEventListener(this);
		this.ramButton.setBounds(1040, 6, 31, 31);
		add(ramButton);
		
		this.teamspeakButton.addEventListener(this);
		this.teamspeakButton.setBounds(892, 536);
		add(teamspeakButton);
		
		this.twitterButton.addEventListener(this);
		this.twitterButton.setBounds(815, 536);
		add(twitterButton);
		
		this.discordButton.addEventListener(this);
		this.discordButton.setBounds(969, 536);
		add(discordButton);
		
		this.webButton.addEventListener(this);
		this.webButton.setBounds(1046, 536);
		add(webButton);
		
		this.problButton.addEventListener(this);
		this.problButton.setBounds(51, 497);
		add(problButton);
		
		this.createaccountButton.addEventListener(this);
		this.createaccountButton.setBounds(241, 497);
		add(createaccountButton);
		
		this.jep.setBounds(474, 126, 627, 395);
		this.jep.setOpaque(false);
		this.jep.setBorder(null);
		this.jep.setEditable(false);
        HTMLEditorKit kit = new HTMLEditorKit();

        this.jep.setEditorKit(kit);

        try {
        	this.jep.setPage(new URL("http://newslauncher.hol.es/"));
        	this.add(jep);
        }
        catch (IOException e) {
        	this.jep.setContentType("text/css;  charset=UTF-8");
        	this.jep.setContentType("text/html; charset=UTF-8");
        	this.jep.setText("imposible de visualiser la page");
 }
 
	}

	@Override
	public void onEvent(SwingerEvent e) {
		if(e.getSource() == playButton) {
			setFieldsEnabled(false);
			/* .length() == 0 ||  passwordField.getText().length() == 0) { */
			if(usernameField.getText().replaceAll(" ", " ").length() == 0 ||  passwordField.getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un pseudo et un mot de passe valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
				setFieldsEnabled(true);
				return;
			}
			
			ramSelector.save();

			Thread t = new Thread() {
				@Override
				public void run() {
				
				 	try {
				 	    Launcher.auth(usernameField.getText(), passwordField.getName()); /*passwordField.getName());*/
				 	    } catch (AuthenticationException e) {
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de se connecter : " + e.getErrorModel().getErrorMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
				 	    setFieldsEnabled(true);
				 	    return;
				 	    }

				 		saver.set("username", usernameField.getText());
					
					try {
						Launcher.update();
						} catch (Exception e) {
							Launcher.interruptThread();
							Launcher.getCrashReporter().catchError(e, "Impossible de mettre a jour !");
						}
					
					try {
						Launcher.launch();
						} catch (LaunchException e) {
							Launcher.getCrashReporter().catchError(e, "Impossible de lancer le jeu !");

						}
				}
			};
			t.start();
		}
	    if(e.getSource() == quitButton)
			Animator.fadeOutFrame(LauncherFrame.getInstance(), 3, new Runnable() {
				public void run() {
					System.exit(0);
				}
			});
		else if(e.getSource() == hideButton)
			LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
	    
		else if(e.getSource() == this.ramButton)
			ramSelector.display();
	    
        else if(e.getSource() == teamspeakButton)
            try {
                Desktop.getDesktop().browse(new URI("ts3server://ts.votrets.frr?port=9987"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        else if(e.getSource() == twitterButton)
            try {
                Desktop.getDesktop().browse(new URI("https://twitter.com/LifeDream_s"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        else if(e.getSource() == discordButton)
            try {
                Desktop.getDesktop().browse(new URI("https://discord.gg/ucTPzxe"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        else if(e.getSource() == webButton)
            try {
                Desktop.getDesktop().browse(new URI("ts3server://ts.votrets.frr?port=9987"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        else if(e.getSource() == problButton)
            try {
                Desktop.getDesktop().browse(new URI("ts3server://ts.votrets.frr?port=9987"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        else if(e.getSource() == createaccountButton)
            try {
                Desktop.getDesktop().browse(new URI("ts3server://ts.votrets.frr?port=9987"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
}
	
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		drawFullsizedImage(graphics, this, background);
	}
	
	private void setFieldsEnabled(boolean enabled) {
		this.usernameField.setEnabled(enabled);
		this.playButton.setEnabled(enabled);
	}
	
	public SColoredBar getProgressBar( ) {
		return this.progressBar;
	}
	
	public void setInfoText(String text) {
		this.infoLabel.setText(text);
	}
	
	public RamSelector getRamSelector() {
		return ramSelector;
	}
	
}
