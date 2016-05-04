//******************************************************************************
//
// File:    HeapPanel.java
// Package: ---
// Unit:    Class HeapPanel.java
//
// This Java source file is copyright (C) 2015 by Alan Kaminsky. All rights
// reserved. For further information, contact the author, Alan Kaminsky, at
// ark@cs.rit.edu.
//
// This Java source file is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by the Free
// Software Foundation; either version 3 of the License, or (at your option) any
// later version.
//
// This Java source file is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// details.
//
// You may obtain a copy of the GNU General Public License on the World Wide Web
// at http://www.gnu.org/licenses/gpl.html.
//
//******************************************************************************

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

/**
 *  Class for a Swing widget displaying a heap of markers.
 * @author Alan Kaminsky
 * @author Kyle Blyth
 * @version 02-Dec-2015
 */

	@SuppressWarnings("serial")
	class HeapPanel
		extends JPanel
		{
		private static final int W = 50;
		private static final int H = 30;
		private static final Color FC = Color.RED;
		private static final Color OC = Color.BLACK;

		private int maxCount;
		private int count;
		private boolean isEnabled;
		private HeapListener listener;

		// Construct a new heap panel.
		public HeapPanel
			(int id,       // Heap panel ID
			 int maxCount) // Maximum number of markers
			{
			this.maxCount = maxCount;
			this.count = maxCount;
			this.isEnabled = true;
			Dimension dim = new Dimension (W, maxCount*H);
			setMinimumSize (dim);
			setMaximumSize (dim);
			setPreferredSize (dim);
			addMouseListener (new MouseAdapter()
				{
				public void mouseClicked (MouseEvent e)
					{
					if (isEnabled && listener != null)
						{
						int objClicked = maxCount - 1 - e.getY()/H;
						int numRemoved = count - objClicked;
						if (numRemoved > 0)
							doRemoveObjects(id, numRemoved);
						}
					}
				});
			}

		// Set this heap panel's listener.
		public void setListener
			(HeapListener listener)
			{
			this.listener = listener;
			}
		
		/**
		 * Handle clicks on heap panels
		 * 
		 * @param id
		 * @param numRemoved
		 */
		private synchronized void doRemoveObjects(int id, int numRemoved) {
			listener.removeObjects (id, numRemoved);
		}

		// Set the number of markers in this heap panel.
		public void setCount
			(int count) // Number of markers
			{
			count = Math.max (0, Math.min (count, maxCount));
			if (this.count != count)
				{
				this.count = count;
				repaint();
				}
			}
		
		// Get the number of markers in this heap panel.
		public int getCount()
			{
				return count;
			}

		// Enable or disable this heap panel.
		public void setEnabled
			(boolean enabled) // True to enable, false to disable
			{
			if (this.isEnabled != enabled)
				{
				this.isEnabled = enabled;
				repaint();
				}
			}

		// Paint this heap panel.
		protected void paintComponent
			(Graphics g) // Graphics context
			{
			super.paintComponent (g);

			// Clone graphics context.
			Graphics2D g2d = (Graphics2D) g.create();

			// Turn on antialiasing.
			g2d.setRenderingHint
				(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);

			// For drawing markers.
			Ellipse2D.Double ellipse = new Ellipse2D.Double();
			ellipse.width = W - 2;
			ellipse.height = H - 2;
			ellipse.x = 1;

			// If enabled, draw filled markers.
			if (isEnabled)
				{
				g2d.setColor (FC);
				for (int i = 0; i < count; ++ i)
					{
					ellipse.y = (maxCount - 1 - i)*H + 1;
					g2d.fill (ellipse);
					}
				}

			// If disabled, draw outlined markers.
			else
				{
				g2d.setColor (OC);
				for (int i = 0; i < count; ++ i)
					{
					ellipse.y = (maxCount - 1 - i)*H + 1;
					g2d.draw (ellipse);
					}
				}
			}
		}