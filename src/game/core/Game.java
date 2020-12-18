package game.core;

import game.Match;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;
import java.util.ArrayList;

public class Game extends JComponent implements IRenderable, IUpdatable
{
	public static final Vector2Int GAME_SIZE = new Vector2Int(1472, 768);
	public static final Vector2Int BOARD_SIZE = new Vector2Int(10, 10);

	private static ArrayList<IUpdatable> updatables = new ArrayList<IUpdatable>();
	private static ArrayList<IRenderable> renderables = new ArrayList<IRenderable>();

	private static Input input;
	private static UI ui;

	private String title;

	private int targetUpdates;
	private int targetRenders;

	private JFrame frame;
	private Timer updateTimer;
	private Timer renderTimer;
	private Timer secondsTimer;


	public Game(String title, int targetUpdates, int targetRenders)
	{
		this.title = title;
		this.targetUpdates = targetUpdates;
		this.targetRenders = targetRenders;

		try
		{
			Resources.Initialize();
		}
		catch (Exception ex)
		{
			System.out.println("Resources could not be loaded");
		}

		input = new Input();
		ui = new UI(this);

		frame = new JFrame(title);

		renderTimer = new Timer();
		updateTimer = new Timer();
		secondsTimer = new Timer();

		setDoubleBuffered(true);
		setPreferredSize(new Dimension(GAME_SIZE.x, GAME_SIZE.y));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(this);
		frame.addKeyListener(input);
		frame.getContentPane().addMouseListener(input);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		var thread = new Thread(() ->
		{
			long lastTime = System.currentTimeMillis();

			while (true)
			{
				if (updateTimer.getDeltaTime() >= 1d / targetUpdates)
				{
					long thisTime = System.currentTimeMillis();
					long deltaTime = thisTime - lastTime;

					update(deltaTime);
					updateTimer.reset();

					lastTime = thisTime;
				}

				if (renderTimer.getDeltaTime() >= 1d / targetRenders)
				{
					repaint();
					renderTimer.reset();
				}

				if (secondsTimer.getDeltaTime() >= 1)
				{
					secondsTimer.reset();
				}

				updateTimer.update();
				renderTimer.update();
				secondsTimer.update();
			}
		});

		thread.start();
	}

	public void paintComponent(Graphics g)
	{
		render(new BoardRenderer(g, new Vector2Int(64, 64), new Vector2Int(768, 64), 64, BOARD_SIZE));
	}

	public void StartMatch()
	{
		var match = new Match(BOARD_SIZE);
	}

	public static void addRenderable(IRenderable renderable)
	{
		renderables.add(renderable);
	}

	public static void removeRenderable(IRenderable renderable)
	{
		renderables.remove(renderable);
	}

	public static void addUpdatable(IUpdatable updatable)
	{
		updatables.add(updatable);
	}

	public static void removeUpdatable(IUpdatable updatable)
	{
		updatables.remove(updatable);
	}

	@Override
	public void render(BoardRenderer renderer)
	{
		for (var renderable : renderables)
		{
			renderable.render(renderer);
		}
	}

	@Override
	public void update(long elapsedMillis)
	{
		for (var updatable : updatables)
		{
			updatable.update(elapsedMillis);
		}
	}
}
