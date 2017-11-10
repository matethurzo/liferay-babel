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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author Mate Thurzo
 */
@RestController
public class SentenceController {

	@RequestMapping(path = "/sentences/process", produces = "text/plain")
	public String process(@RequestParam(value = "sentence", required = true) String sentence) {
		String[] words = sentence.split("\\W");

		Arrays.stream(words)
			.filter((w) -> !"".equals(w))
			.forEach(
				(word) ->
					_neo4jDatabase.run(
						"MERGE (w:Word {text: '" + word + "'}) ON CREATE SET w.count = 1 ON MATCH SET w.count = " +
							"w.count + 1")
			);

		for (int i = 0; i < words.length; i++) {
			if (i == (words.length - 1)) {
				break;
			}

			_neo4jDatabase.run(
				"MATCH (w1:Word {text: '" + words[i] +
					"'}) WITH w1 MATCH (w2:Word {text: '" + words[i + 1] +
						"'}) WITH w1, w2 MERGE (w1)-[r:NEXT]->(w2) ON CREATE SET r.count = 1 ON MATCH SET r.count = " +
							"r.count + 1");
		}

		return "success";
	}

	@Autowired
	private Neo4jDatabase _neo4jDatabase;

}
