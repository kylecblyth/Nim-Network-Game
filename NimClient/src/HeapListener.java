//******************************************************************************
//
// File:    HeapListener.java
// Package: ---
// Unit:    Class HeapListener.java
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

/**
 *  Interface for a listener for HeapPanel events.
 * @author Alan Kaminsky
 * @version 07-Oct-2015
 */

	interface HeapListener
		{
		// Report that markers are to be removed from a heap.
		public void removeObjects
			(int id,          // Heap panel ID
			 int numRemoved); // Number of markers to be removed
		}