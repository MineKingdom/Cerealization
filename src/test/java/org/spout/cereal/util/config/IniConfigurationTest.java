/*
 * This file is part of Cerealization.
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 * Cerealization is licensed under the Spout License Version 1.
 *
 * Cerealization is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Cerealization is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.cereal.util.config;

import org.junit.Test;

import org.spout.cereal.config.ConfigurationException;
import org.spout.cereal.config.ConfigurationNode;
import org.spout.cereal.config.commented.CommentedConfigurationNode;
import org.spout.cereal.config.ini.StringLoadingIniConfiguration;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static org.spout.cereal.config.commented.CommentedConfigurationNode.LINE_SEPARATOR;

public class IniConfigurationTest {
	@Test
	public void testBasicLoading() throws ConfigurationException {
		StringLoadingIniConfiguration subject = new StringLoadingIniConfiguration("[section]" + LINE_SEPARATOR + "node = value" + LINE_SEPARATOR);
		subject.load();
		ConfigurationNode sectionNode = subject.getNode("section");
		assertNotNull(sectionNode);
		assertTrue(sectionNode.isAttached());
		assertTrue(sectionNode.hasChildren());
		assertEquals("value", subject.getNode("section.node").getString());
	}

	@Test
	public void testBasicSaving() throws ConfigurationException {
		StringLoadingIniConfiguration subject = new StringLoadingIniConfiguration(null);
		subject.getNode("section.node").setValue("value");
		subject.save();
		assertEquals("[section]" + LINE_SEPARATOR + "node=value" + LINE_SEPARATOR, subject.getValue());
	}

	@Test
	public void testCommentLoading() throws ConfigurationException {
		StringLoadingIniConfiguration subject = new StringLoadingIniConfiguration("# This is the first section!" + LINE_SEPARATOR + "[section]" + LINE_SEPARATOR + "# This is a node!" + LINE_SEPARATOR + "# With a multiline comment!" + LINE_SEPARATOR + "node=value" + LINE_SEPARATOR);
		subject.load();
		ConfigurationNode node = subject.getNode("section");
		assertArrayEquals(new String[] {"This is the first section!"}, ((CommentedConfigurationNode) node).getComment());
		node = subject.getNode("section.node");
		assertArrayEquals(new String[] {"This is a node!", "With a multiline comment!"}, ((CommentedConfigurationNode) node).getComment());
	}

	@Test
	public void testCommentSaving() throws ConfigurationException {
		StringLoadingIniConfiguration subject = new StringLoadingIniConfiguration(null);
		subject.getNode("section").setComment("Hello", "World");
		subject.getNode("section", "node").setValue("value");
		subject.getNode("section", "node").setComment("Node Comment");
		subject.save();
		assertEquals("# Hello" + LINE_SEPARATOR + "# World" + LINE_SEPARATOR + "[section]" + LINE_SEPARATOR + "# Node Comment" + LINE_SEPARATOR + "node=value" + LINE_SEPARATOR, subject.getValue());
	}
}
