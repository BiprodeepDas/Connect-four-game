package com.example.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

	private static final int rows=6;
	private static final int co=7;
	private static final int circledia=80;
	private static final String disccol1="#24303E";
	private static final String getDisccol2="#4CAA88";


	private static String p1;
	private static String p2;
	private boolean isp1turn=true;

	private Disc[][] insertedDiscArray=new Disc[rows][co];

	private boolean isAllowedToEnter=true;


	@FXML
	public TextField pl1;

	@FXML
	public TextField pl2;

	@FXML
	public Button snb;

	@FXML
	public GridPane rootid;

	@FXML
	public Pane insertDiscId;

	@FXML
	public Label playerLabelid;


	public void playGround(){

		Platform.runLater(()-> snb.requestFocus());
		Shape rectangleWithHoles = gameStructuralGrid();
		rootid.add(rectangleWithHoles, 0, 1);
		List<Rectangle> rectangleList=ClickableColoumns();

		for(Rectangle rectangle:rectangleList) {
			rootid.add(rectangle, 0, 1);
		}

		snb.setOnAction(event -> {
			p1=pl1.getText();
			p2=pl2.getText();
		});
	}
	private Shape gameStructuralGrid(){
		Shape rectangleWithHoles = new Rectangle((co + 1) * circledia, (rows + 1) * circledia);

		for (int row = 0; row < rows; row++) {

			for (int col = 0; col < co; col++) {
				Circle circle = new Circle();
				circle.setRadius(circledia / 2);
				circle.setCenterX(circledia / 2);
				circle.setCenterY(circledia / 2);
				circle.setSmooth(true);
				circle.setSmooth(true);

				circle.setTranslateX(col * (circledia + 5) + circledia / 4);
				circle.setTranslateY(row * (circledia + 5) + circledia / 4);

				rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
			}
		}

		rectangleWithHoles.setFill(Color.WHITE);

		return rectangleWithHoles;
	}
	public List<Rectangle> ClickableColoumns(){

		List<Rectangle> rectanglelist=new ArrayList<>();

		for(int cols=0;cols<co;cols++) {
			Rectangle rectangle = new Rectangle(circledia, (rows + 1) * circledia);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(cols * (circledia + 5)+circledia / 4);

			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
			rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));

			final int coloumn=cols;
			rectangle.setOnMouseClicked(event ->{
				if(isAllowedToEnter) {
					isAllowedToEnter=false;
					InsertDisc(new Disc(isp1turn), coloumn);
				}
			});

			rectanglelist.add(rectangle);
		}
		return rectanglelist;
	}

	private void InsertDisc(Disc disc, int coloumn) {

		int row=rows-1;
		while(row >= 0){
			if(isDiscIfpresent(row,coloumn)==null){
				break;
			}
			row--;

		}
		if(row<0){
			return;
		}

		insertedDiscArray[row][coloumn]=disc;
		insertDiscId.getChildren().add(disc);

		int cureentRow=row;

		disc.setTranslateX(coloumn * (circledia + 5)+circledia / 4);

		TranslateTransition transition=new TranslateTransition(Duration.seconds(1),disc);

		transition.setToY(row * (circledia + 5) + circledia / 4);

		transition.setOnFinished(event -> {

			isAllowedToEnter=true;

			if(gameEnded(cureentRow,coloumn)){
				gameOver();
				return;
			}

			isp1turn=!isp1turn;
			playerLabelid.setText(isp1turn? p1+"'s Turn" : p2+"'s Turn");
		});

		transition.play();
	}

	private void gameOver() {

		String winner= isp1turn? p1 : p2;
		System.out.println("Winner is "+winner);

		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setTitle("Connect Four");
		a.setHeaderText("The Winner is "+winner);
		a.setContentText("Want to Play Again ?");

		ButtonType ysbtn=new ButtonType("Yes");
		ButtonType nobtn=new ButtonType("No");

		a.getButtonTypes().setAll(ysbtn,nobtn);

		Platform.runLater(()->{

			Optional<ButtonType> btnClicked=a.showAndWait();
			if(btnClicked.isPresent() && btnClicked.get()==ysbtn){
				resetGame();
			}else{
				Platform.exit();
				System.exit(0);
			}
		});
	}

	public void resetGame() {

		insertDiscId.getChildren().clear();
		for (int row=0;row<insertedDiscArray.length;row++){
			for (int col=0;col<insertedDiscArray[row].length;col++){
				insertedDiscArray[row][col]=null;
			}
		}
		isp1turn=true;
		playerLabelid.setText("Player One");
		playGround();
	}

	private boolean gameEnded(int cureentRow, int coloumn) {

		List<Point2D> verticalPoints= IntStream.rangeClosed(cureentRow-3,cureentRow+3)
				                      .mapToObj(r -> new Point2D(r,coloumn))
				                      .toList();

		List<Point2D> horizontalPoints= IntStream.rangeClosed(coloumn-3,coloumn+3)
				.mapToObj(c -> new Point2D(cureentRow,c))
				.toList();

		Point2D startingPoint1=new Point2D(cureentRow-3,coloumn+3);
		List<Point2D> diagonal1Points = IntStream.rangeClosed(0,6)
				                        .mapToObj(i -> startingPoint1.add(i,-i)).toList();

		Point2D startingPoint2=new Point2D(cureentRow-3,coloumn-3);
		List<Point2D> diagonal2Points = IntStream.rangeClosed(0,6)
				.mapToObj(i -> startingPoint2.add(i,i)).toList();


		boolean isEnded=checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
				        || checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);

		return isEnded;

	}

	private boolean checkCombinations(List<Point2D> points) {
		
		int chain=0;
		for (Point2D point:points) {

			int rowIndexForArray= (int) point.getX();
			int coloumnIndexForArray= (int) point.getY();

			Disc disc=isDiscIfpresent(rowIndexForArray,coloumnIndexForArray);

			if(disc != null &&  disc.IsPlayerOneMove == isp1turn){
				chain++;
				if(chain==4){
					return true;
				}
			}else{
				chain=0;
			}
		}
		return false;
		
	}

	private Disc isDiscIfpresent(int row,int coloumn){
		if(row >= rows || row<0 || coloumn >= co || coloumn < 0){
			return null;
		}else {
			return insertedDiscArray [row][coloumn];
		}
	}

	private static class Disc extends Circle{

		private final boolean IsPlayerOneMove;
		private Disc(boolean IsPlayerOneMove) {
			this.IsPlayerOneMove=IsPlayerOneMove;
			setRadius(circledia/2);
			setFill(IsPlayerOneMove? Color.valueOf(disccol1):Color.valueOf(getDisccol2));
			setCenterY(circledia/2);
			setCenterX(circledia/2);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

	}
}