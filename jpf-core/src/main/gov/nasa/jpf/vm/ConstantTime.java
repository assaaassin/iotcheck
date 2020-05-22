/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * The Java Pathfinder core (jpf-core) platform is licensed under the
 * Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package gov.nasa.jpf.vm;

import gov.nasa.jpf.Config;

/**
 * simple delegating TimeModel implementation that just returns System time.
 * While elapsed time is not path sensitive, it is at least strictly increasing
 * along any given path
 */
public class ConstantTime implements TimeModel {

  public ConstantTime(VM vm, Config conf){
    // we don't need these, but it speeds up VM initialization
  }
  
  @Override
  public long currentTimeMillis() {
    return 1564694080896L;
  }

  @Override
  public long nanoTime() {
    return 365341665679019L;
  }
}
