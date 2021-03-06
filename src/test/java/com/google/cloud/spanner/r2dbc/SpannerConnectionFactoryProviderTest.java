/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.spanner.r2dbc;

import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.junit.Test;

/**
 * Unit test for {@link SpannerConnectionFactoryProvider}.
 */
public class SpannerConnectionFactoryProviderTest {

  @Test
  public void testCreate() {
    SpannerConnectionFactoryProvider spannerConnectionFactoryProvider =
        new SpannerConnectionFactoryProvider();
    ConnectionFactory spannerConnectionFactory = spannerConnectionFactoryProvider
        .create(null);

    assertThat(spannerConnectionFactory).isNotNull();
  }

  @Test
  public void testSupportsThrowsExceptionOnNullOptions() {
    SpannerConnectionFactoryProvider spannerConnectionFactoryProvider =
        new SpannerConnectionFactoryProvider();
    assertThatThrownBy(() -> {
      spannerConnectionFactoryProvider.supports(null);
    }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("connectionFactoryOptions must not be null");
  }

  @Test
  public void testSupportsReturnsFalseWhenNoDriverInOptions() {
    SpannerConnectionFactoryProvider spannerConnectionFactoryProvider =
        new SpannerConnectionFactoryProvider();
    assertFalse(spannerConnectionFactoryProvider.supports(
        ConnectionFactoryOptions.builder().build()));
  }

  @Test
  public void testSupportsReturnsFalseWhenWrongDriverInOptions() {
    SpannerConnectionFactoryProvider spannerConnectionFactoryProvider =
        new SpannerConnectionFactoryProvider();
    assertFalse(spannerConnectionFactoryProvider.supports(buildOptions("not spanner")));
  }

  @Test
  public void testSupportsReturnsTrueWhenCorrectDriverInOptions() {
    SpannerConnectionFactoryProvider spannerConnectionFactoryProvider =
        new SpannerConnectionFactoryProvider();
    assertTrue(spannerConnectionFactoryProvider.supports(buildOptions("spanner")));
  }

  @Test
  public void testR2dbcFindsSpannerConnectionFactoryProvider() {
    ConnectionFactory connectionFactory =
        ConnectionFactories.get(ConnectionFactoryOptions.builder()
            .option(DRIVER, "spanner")
            .build());

    assertThat(connectionFactory).isInstanceOf(SpannerConnectionFactory.class);
  }

  private static ConnectionFactoryOptions buildOptions(String driverName) {
    return ConnectionFactoryOptions.builder()
        .option(ConnectionFactoryOptions.DRIVER, driverName)
        .build();
  }
}
