/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.servicecomb.saga.omega.transaction.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.servicecomb.saga.omega.transaction.annotations.Compensable;

@Component
class TransactionalUserService {
  static final String ILLEGAL_USER = "Illegal User";
  private final UserRepository userRepository;

  @Autowired
  TransactionalUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Compensable(compensationMethod = "delete")
  User add(User user) {
    if (ILLEGAL_USER.equals(user.username())) {
      throw new IllegalArgumentException("User is illegal");
    }
    return userRepository.save(user);
  }

  void delete(User user) {
    userRepository.delete(user.id());
  }
}