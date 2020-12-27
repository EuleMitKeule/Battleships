package game.core;

import game.Match;
import game.Resources;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;
import java.util.ArrayList;

public class Game extends JComponent implements IRenderable, IUpdatable
{
	/**
	 *
	 */
	private static final long serialVersionUID = -6081721995438708904L;

	public static final Vector2Int GAME_SIZE = new Vector2Int(1472, 768);
	public static final Vector2Int BOARD_SIZE = new Vector2Int(10, 10);

	private static ArrayList<IUpdatable> updatables = new ArrayList<IUpdatable>();
	private static ArrayList<IRenderable> renderables = new ArrayList<IRenderable>();

	private static Input input;

	private JFrame frame;
	private Timer updateTimer;
	private Timer renderTimer;
	private Timer secondsTimer;

	/**
	 * @param title The window title
	 * @param targetUpdates The targeted update frames per second
	 * @param targetRenders The targeted render frames per second
	 */
	public Game(String title, int targetUpdates, int targetRenders)
	{
		try
		{
			Resources.Initialize();
		}
		catch (Exception ex)
		{
			System.out.println("Resources could not be loaded");
		}

		input = new Input();
		new Match(this, new Vector2Int(64, 64), new Vector2Int(768, 64), 64, BOARD_SIZE);

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

	/**
	 * Gets invoked every render frame
	 * @param graphics The graphics context
	 */
	public void paintComponent(Graphics graphics)
	{
		render(new BoardRenderer(graphics, new Vector2Int(64, 64), new Vector2Int(768, 64), 64));
	}

	/**
	 * Adds an IRenderable to the list of renderable objects
	 * @param renderable The renderable to add
	 */
	public static void addRenderable(IRenderable renderable)
	{
		renderables.add(renderable);
	}

	/**
	 * Removes an IRenderable from the list of renderable objects
	 * @param renderable The renderable to remove
	 */
	public static void removeRenderable(IRenderable renderable)
	{
		renderables.remove(renderable);
	}

	/**
	 * Adds an IUpdatable to the list of updatable objects
	 * @param updatable The updatable to add
	 */
	public static void addUpdatable(IUpdatable updatable)
	{
		updatables.add(updatable);
	}

	/**
	 * Removes an IUpdatable from the list of updatable objects
	 * @param updatable The updatable to remove
	 */
	public static void removeUpdatable(IUpdatable updatable)
	{
		updatables.remove(updatable);
	}

	/**
	 * Invokes the render method on every renderable object
	 * @param renderer The renderer context
	 */
	@Override
	public void render(BoardRenderer renderer)
	{
		for (var renderable : renderables)
		{
			renderable.render(renderer);
		}
	}

	/**
	 * Invokes the update method on every updatable object
	 * @param elapsedMillis The time passed since last frame in milliseconds
	 */
	@Override
	public void update(long elapsedMillis)
	{
		for (var updatable : updatables)
		{
			updatable.update(elapsedMillis);
		}
	}
}
