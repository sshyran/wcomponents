define(["intern!object", "intern/chai!assert", "./resources/test.utils!"],
	function (registerSuite, assert, testutils) {
		"use strict";

		var i18n;
		registerSuite({
			name: "i18n",
			setup: function() {
				return testutils.setupHelper(["wc/i18n/i18n"], function(obj) {
					i18n = obj;
				});
			},
			testGet: function() {
				/*
				* In this test we simply test that we are getting a message when we ask
				* for one that we know exists.
				*/
				var key = "chars_remaining",
					result = i18n.get(key);
				assert.isTrue(result.length > 0);
			},
			testGetWithFormattingArgs: function() {
				/*
				* In this test we chose a message that accepts at least one printf arg
				* and check that it is inserted into the string.
				*/
				var arg = 3,
					key = "chars_remaining",
					result = i18n.get(key);
				assert.isTrue(result.indexOf(arg) === -1);
				result = i18n.get(key, arg);
				assert.isTrue(result.indexOf(arg) >= 0);
			},
			testGetWithSuperfluousFormattingArgs: function() {
				/*
				* In this test we chose a message that doesn't accept a printf arg,
				* pass it one anyway, and check that it is not inserted into the string.
				*/
				var arg = 3,
					key = "day4",
					result = i18n.get(key);
				assert.isTrue(result.indexOf(arg) === -1);
				result = i18n.get(key, arg);
				assert.isTrue(result.indexOf(arg) === -1);
			},
			testGetNoMessageFoundReturnsKey: function() {
				/*
				* In this test we test that the key is returned when we
				* ask for a message that does not exist.
				*/
				var key = "fukung_kungfu",
					result = i18n.get(key);
				assert.isTrue(result === key);
			},
			testGetWithFormattingArgsAndZero: function() {
				/*
				 * This test checks that primative zero is accepted as an arg.
				* This is based on a real-world defect we encountered.
				*/
				var arg = 0,
					key = "chars_remaining",
					result = i18n.get(key);
				assert.isTrue(result.indexOf(arg) === -1);
				result = i18n.get(key, arg);
				assert.isTrue(result.indexOf(arg) >= 0);
			}
		});
	});
