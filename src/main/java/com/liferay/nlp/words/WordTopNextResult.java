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

import java.util.List;

/**
 * @author Mate Thurzo
 */
public class WordTopNextResult {

	public WordTopNextResult(String word, List<String> next) {
		_next = next;
		_word = word;
	}

	public String getWord() {
		return _word;
	}

	public List<String> getTopNext() {
		return _next;
	}

	private final String _word;
	private final List<String> _next;

}
