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
package org.createnet.raptor.auth.authorization.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import org.createnet.raptor.auth.AuthConfiguration;
import org.createnet.raptor.auth.AuthHttpClient;
import org.createnet.raptor.auth.authorization.AbstractAuthorization;
import org.createnet.raptor.auth.entity.AuthorizationRequest;
import org.createnet.raptor.models.objects.RaptorComponent;
import org.createnet.raptor.models.objects.ServiceObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <lcapra@create-net.org>
 */
public class TokenAuthorization extends AbstractAuthorization {
  
  final ObjectMapper mapper = new ObjectMapper();
      
  final private Logger logger = LoggerFactory.getLogger(TokenAuthorization.class);
  final private AuthHttpClient client = new AuthHttpClient();

  @Override
  public boolean isAuthorized(String accessToken, ServiceObject obj, Permission op) {

    try {
      
      String id = obj == null ? null : obj.getId();
      
      logger.debug("Check authorization for object {} for permission {}", id, op.toString());
      
      String response = request(accessToken, id, op.toString());

      JsonNode node = mapper.readTree(response);
      boolean allowed = node.get("result").booleanValue();

      logger.debug("User {} allowed to {} on {}", (!allowed ? "NOT" : ""), op.toString(), id);

      return allowed;

    } catch (IOException | AuthHttpClient.ClientException ex) {
      logger.error("Error checking authorization request", ex);
      throw new AuthorizationException(ex);
    }
  }

  @Override
  public void initialize(AuthConfiguration configuration) {
    super.initialize(configuration);
    client.setConfig(configuration);
  }

  protected String request(String accessToken, String id, String permission) {

    AuthorizationRequest authzreq = new AuthorizationRequest();
    authzreq.permission = permission;
    authzreq.objectId = id;

    String payload;
      try {
          payload = mapper.writeValueAsString(authzreq);
      } catch (JsonProcessingException ex) {
          throw new RaptorComponent.ParserException(ex);
      }
    
    return client.check(accessToken, payload);
  }

}
