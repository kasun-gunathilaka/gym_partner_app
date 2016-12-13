package com.kasungunathilaka.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

// </summary>
// Source File		: IBusiness.java
// Package 			: com.kasungunathilaka.business
// Description		: Business Interface class
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 17 April 2016    Kasun Gunathilak			    Created
// </remarks>
//
// <license>
// Copyright 2016 Kasun Gunathilaka
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// </license>

public interface IBusiness<T> {

    long insert(T value);

    ArrayList<T> getAll() throws ParseException;

    T getById(int id) throws ParseException;

    void delete(int id);

    void update(T value) throws Exception;

    void close();
}
