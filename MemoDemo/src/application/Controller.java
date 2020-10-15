package application;


import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Controller {
	
	@FXML
	private AnchorPane loginAncPane;
	@FXML
	private TextField txtUsername;
	@FXML
	private TextField txtPassword;
	@FXML
	private Button btnCreateNewUser;
	@FXML
	private Pane CNUPane;
	@FXML
	private Button btnCreateUserCNU;
	@FXML
	private Button btnCancelCNU;
	@FXML
	private Label lblLoginError;
	@FXML
	private TextField txtUsernameCNU;
	@FXML
	private Label errPW;
	@FXML
	private Label errUsername;
	@FXML
	private PasswordField txtPwCNU;
	@FXML
	private PasswordField txtPw2CNU;
	@FXML
	private TextField txtTaskName;
	@FXML
	private TextField txtDay;
	@FXML
	private TextField txtMonth;
	@FXML
	private TextArea ta;
	@FXML
	private Button btnAddTask;
	@FXML
	private Button btnNewTask;
	@FXML
	private Button btnCancelCNT;
	@FXML
	private Pane CNTPane;
	@FXML
	private CheckBox cbImportant;
	@FXML
	private ListView<String> lv;
	@FXML
	private Button btnRefreshList;
	@FXML
	private Button btnDeleteTask;
	@FXML
	private Label lbDescription;
	@FXML
	private RadioButton rbDueDate;
	@FXML
	private RadioButton rbImportance;
	@FXML
	private RadioButton rbEntryDate;

	
	
	
	
	DBConnection conn = new DBConnection();
	static int key;
	String[] months= {"January","February","March","April","May","June","July","August","September","October","November","December"};
	
	
	public void login(ActionEvent event)throws Exception {
		
		String usernameEntered =txtUsername.getText();
		String passwordEntered =txtPassword.getText();
		ResultSet rs = conn.getRS("SELECT * FROM \"User\"");
            	
            // Iterate through the data in the result set and compare it.
        while (rs.next()) {
        	if(usernameEntered.equals(rs.getString("Username")) && passwordEntered.equals(rs.getString("Password"))) {
        		
        		int ID = rs.getInt("ID");
        		key=ID;
        		
        	 //Change scene to Main.fxml
        		AnchorPane pane = FXMLLoader.load(getClass().getResource("Main.fxml"));
        		loginAncPane.getChildren().setAll(pane);
        		
        		}
        	else {
        		
        		lblLoginError.setVisible(true);
        		
        	}
        }   
	}
	
	public void CNUPaneControl(ActionEvent event) throws Exception {
		
        if (event.getSource() == btnCreateNewUser) {
        	
        	CNUPane.setVisible(true);
        	
        } else if (event.getSource() == btnCreateUserCNU ) {
        	
        	ResultSet rs = conn.getRS("SELECT * FROM \"User\"");
        	String username = txtUsernameCNU.getText();
    		String pwField1 = txtPwCNU.getText();
    		String pwField2 = txtPw2CNU.getText();
        	boolean UsernameCheck = true;
        		
        		while (rs.next()) {
        			if(username.equals(rs.getString("Username"))) {
        				errUsername.setVisible(true);
        				UsernameCheck=false;
        				break;
        				}
        			}
        		
        	if(!UsernameCheck) {
        		errUsername.setVisible(true);
        	}
        	else if(!pwField1.equals(pwField2)) {
        		errPW.setVisible(true);
        	}
        	else if(pwField1.equals(pwField2) && UsernameCheck  ) {
        		conn.insertQuery("INSERT INTO \"User\" VALUES ('"+username+"','"+pwField1+"')");
        		CNUPane.setVisible(false);
        	}
        	
        }
        else {
        	txtUsernameCNU.setText("");
        	txtPwCNU.setText("");
        	txtPw2CNU.setText("");
        	errUsername.setVisible(false);
        	errPW.setVisible(false);
        	CNUPane.setVisible(false);
        }
        
    }
	
	public void CNTPaneControl(ActionEvent event) throws Exception {
		
        if (event.getSource() == btnNewTask) {
        	
        	CNTPane.setVisible(true);
        	
        } else if (event.getSource() == btnAddTask ) {

        	if(txtDay.getText().equals("") ||  txtMonth.getText().equals("") || Integer.parseInt(txtDay.getText())>31  || Integer.parseInt(txtMonth.getText()) > 12) {
        		ta.setText("Error: Enter valid day and month!");
        		}
        	else {
        	 
        	if(!txtTaskName.getText().equals("")) {
        		int cBox=0;
        		if(cbImportant.isSelected()){cBox=1;}
        		
        		conn.insertQuery("INSERT INTO Tasks VALUES ('"+txtTaskName.getText()+"','"+txtDay.getText()+"','"+txtMonth.getText()+"','"+ta.getText()+"',"+cBox+","+key+")");
        		txtTaskName.setText("");
            	txtDay.setText("");
            	txtMonth.setText("");
            	ta.setText("");
            	cbImportant.setSelected(false);
        		CNTPane.setVisible(false);
        	}   
        	else {
    			ta.setText("Error: Must enter task name!");
    		}
        	}
        }
        else {
        	txtTaskName.setText("");
        	txtDay.setText("");
        	txtMonth.setText("");
        	ta.setText("");
        	cbImportant.setSelected(false);
        	CNTPane.setVisible(false);
        }
        
    }
	
	public void refreshList(ActionEvent event) throws Exception {
		
		ResultSet rs =conn.getRS("SELECT * FROM Tasks WHERE \"User\"="+key+"");
		lv.getItems().clear();	
		
		while(rs.next()) {
			lv.getItems().addAll(rs.getString("ID")+"\\ "+rs.getString("Name"));
		}	
		rbImportance.setSelected(false);
		rbDueDate.setSelected(false);
		
	}
	
	public void deleteTask(ActionEvent event) throws Exception {
		
		String ID =lv.getSelectionModel().getSelectedItem().substring(0, lv.getSelectionModel().getSelectedItem().indexOf("\\"));
		conn.insertQuery("DELETE FROM Tasks WHERE ID="+ID+"");
		refreshList(event);
		
		
	}

	public void showDescription(MouseEvent event) throws Exception {
		
		String ID=lv.getSelectionModel().getSelectedItem().substring(0, lv.getSelectionModel().getSelectedItem().indexOf("\\"));
		ResultSet rs =conn.getRS("SELECT * FROM Tasks WHERE ID="+ID+"");
		String description ="";
		while(rs.next()) {
			
			String d = ""+rs.getBoolean("Important")+"";
			if(d.equals("true")) {
				 lbDescription.setTextFill(Color.web("red"));
				 description="Due "+months[Integer.parseInt(rs.getString("Month"))-1]+" "+rs.getString("day")+".\n"+rs.getString("Description")+"\n\nThis task is important.";
			}
			else {
				 lbDescription.setTextFill(Color.web("black"));
				 description="Due "+months[Integer.parseInt(rs.getString("Month"))-1]+" "+rs.getString("day")+".\n"+rs.getString("Description");
			}
			
		
			
		}
		
		lbDescription.setText(description);
		
	}
	
	public void sortList(ActionEvent event) throws Exception {
		
		lv.getItems().clear();	
		
        if (event.getSource() == rbDueDate) {
        	ResultSet rs =conn.getRS("SELECT * FROM Tasks WHERE \"User\"="+key+" ORDER BY \"Month\" ASC,\"day\" ASC");
    		while(rs.next()) {
    			lv.getItems().addAll(rs.getString("ID")+"\\ "+rs.getString("Name"));
    		}	
    		
    			rbImportance.setSelected(false);
    			rbEntryDate.setSelected(false);
    		
        	
        	
        } else if (event.getSource() == rbImportance ) {
        	
          	ResultSet rs =conn.getRS("SELECT * FROM Tasks WHERE \"User\"="+key+" ORDER BY Important DESC, \"Month\" ASC, \"day\" ASC");
    		while(rs.next()) {
    			lv.getItems().addAll(rs.getString("ID")+"\\ "+rs.getString("Name"));
    		}	
			rbDueDate.setSelected(false);
			rbEntryDate.setSelected(false);
        	
        	
        }
        else {
        	ResultSet rs =conn.getRS("SELECT * FROM Tasks WHERE \"User\"="+key+" ORDER BY ID");
        	while(rs.next()) {
    			lv.getItems().addAll(rs.getString("ID")+"\\ "+rs.getString("Name"));
    		}	
			rbImportance.setSelected(false);
			rbDueDate.setSelected(false);
        }
        
    }

}
