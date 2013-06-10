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
package com.github.pmerienne.trident.cf;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import storm.trident.operation.TridentCollector;
import storm.trident.state.BaseQueryFunction;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

import com.github.pmerienne.trident.cf.state.CFState;

public class GetUsersWithPreferenceQuery extends BaseQueryFunction<CFState, Set<Long>> {

	private static final long serialVersionUID = -6608788984829246979L;

	@Override
	public List<Set<Long>> batchRetrieve(CFState state, List<TridentTuple> tuples) {
		List<Set<Long>> results = new ArrayList<Set<Long>>(tuples.size());

		long item;
		Set<Long> users;
		for (TridentTuple tuple : tuples) {
			item = tuple.getLong(1);
			users = state.getUsersWithPreferenceFor(item);
			results.add(users);
		}

		return results;
	}

	@Override
	public void execute(TridentTuple tuple, Set<Long> users, TridentCollector collector) {
		long user = tuple.getLong(0);
		for (long otherUser : users) {
			if (user != otherUser) {
				collector.emit(new Values(otherUser));
			}
		}
	}
}
