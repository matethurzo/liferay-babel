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

package com.liferay.nlp.words;

import com.liferay.nlp.Neo4jDatabase;
import org.neo4j.graphdb.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Mate Thurzo
 */
@RestController
public class WordController {

	@RequestMapping(path = "/words/reset")
	public void reset() {
		_neo4jDatabase.run("MATCH (n) DETACH DELETE n");
	}

	@RequestMapping(path = "/words/count")
	public @ResponseBody WordCountResult wordCount(@RequestParam(value = "word", required = true) String word) {
		Result result = _neo4jDatabase.run("MATCH (w:Word) WHERE w.text = '" + word + "' RETURN w.count AS count");

		if (result.hasNext()) {
			Map<String, Object> resultMap = result.next();

			return new WordCountResult(word, (long)resultMap.get("count"));
		}

		return null;
	}

	@RequestMapping(path = "/words/topprevious")
	public @ResponseBody WordTopPreviousResult topPrevious(@RequestParam(value = "word", required = true) String word) {
		Result result = _neo4jDatabase.run(
			"match (w:Word {text: '" + word + "'})<-[r:NEXT]-() with max(r.count) as countmax, w\n" +
				"match (word:Word)<-[n:NEXT]-(w2:Word) where word.text = w.text and n.count = countmax\n" +
					"return w2.text");

		Stream<Map<String, Object>> resultStream = StreamSupport.stream(
			Spliterators.spliteratorUnknownSize(result, Spliterator.ORDERED), false);

		List<String> topPreviousResults = resultStream
			.flatMap((m) -> m.values().stream())
			.map((o) -> (String)o).collect(Collectors.toList());

		return new WordTopPreviousResult(word, topPreviousResults);
	}

	@RequestMapping(path = "/words/topnext")
	public @ResponseBody WordTopNextResult topNext(@RequestParam(value = "word", required = true) String word) {
		Result result = _neo4jDatabase.run(
			"match (w:Word {text: '" + word + "'})-[r:NEXT]->() with max(r.count) as countmax, w\n" +
				"match (word:Word)-[n:NEXT]->(w2:Word) where word.text = w.text and n.count = countmax\n" +
					"return w2.text");

		Stream<Map<String, Object>> resultStream = StreamSupport.stream(
			Spliterators.spliteratorUnknownSize(result, Spliterator.ORDERED), false);

		List<String> topNextResults = resultStream
			.flatMap((m) -> m.values().stream())
			.map((o) -> (String)o).collect(Collectors.toList());

		return new WordTopNextResult(word, topNextResults);
	}

	@Autowired
	private Neo4jDatabase _neo4jDatabase;

}
