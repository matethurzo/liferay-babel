/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.nlp;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @author Mate Thurzo
 */
@Service
public class Neo4jDatabase {

	@PostConstruct
	public void init() {
		File graphDbDir = new File("graphdb");

		_graphDatabaseService = new GraphDatabaseFactory().newEmbeddedDatabase(graphDbDir);

		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				_graphDatabaseService.shutdown();
			}

		});
	}

	public GraphDatabaseService getService() {
		return _graphDatabaseService;
	}

	public Result run(String cypher) {
		return _graphDatabaseService.execute(cypher);
	}

	private GraphDatabaseService _graphDatabaseService;

}
