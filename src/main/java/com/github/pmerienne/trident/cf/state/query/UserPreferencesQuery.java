/**
 * Copyright 2013-2015 Pierre Merienne
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pmerienne.trident.cf.state.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import storm.trident.operation.TridentCollector;
import storm.trident.state.BaseQueryFunction;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

import com.github.pmerienne.trident.cf.state.SetMultiMapState;

public class UserPreferencesQuery extends BaseQueryFunction<SetMultiMapState<Long, Long>, Set<Long>> {

	private static final long serialVersionUID = 9213616343976422150L;

	@Override
	public List<Set<Long>> batchRetrieve(SetMultiMapState<Long, Long> state, List<TridentTuple> tuples) {
		List<Set<Long>> results = new ArrayList<Set<Long>>();

		Set<Long> preferences;
		long user;
		for (TridentTuple tuple : tuples) {
			user = tuple.getLong(0);
			preferences = state.get(user);
			results.add(preferences);
		}

		return results;
	}

	@Override
	public void execute(TridentTuple tuple, Set<Long> result, TridentCollector collector) {
		collector.emit(new Values(result));
	}

}
