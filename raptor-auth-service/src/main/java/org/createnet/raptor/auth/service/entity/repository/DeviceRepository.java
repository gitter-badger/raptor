/*
 * Copyright 2016 CREATE-NET
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.createnet.raptor.auth.service.entity.repository;

import org.createnet.raptor.auth.service.entity.Device;
import org.createnet.raptor.auth.service.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Luca Capra <lcapra@create-net.org>
 */
public interface DeviceRepository extends CrudRepository<Device, Long> {

  Device findByUuid(String uuid);
  Device findByOwner(User owner);

  @Transactional
  @Override
  public void delete(Device entity);

  @Transactional
  @Override
  public void delete(Long id);

  @Override
  public boolean exists(Long id);

  @Override
  public Device findOne(Long id);

  @Transactional
  @Override
  public <S extends Device> S save(S entity);
  
}
