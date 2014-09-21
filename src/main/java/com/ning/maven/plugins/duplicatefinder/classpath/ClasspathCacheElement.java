/*
 * Copyright 2010 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.ning.maven.plugins.duplicatefinder.classpath;

import java.io.File;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

class ClasspathCacheElement
{
    private final File element;
    private final ImmutableSet<String> classes;
    private final ImmutableSet<String> resources;

    public static ClasspathCacheElement.Builder builder(File element)
    {
        return new Builder(element);
    }

    private ClasspathCacheElement(final File element, final ImmutableSet<String> classes, final ImmutableSet<String> resources)
    {
        this.element = element;
        this.classes = classes;
        this.resources = resources;
    }

    void putClasses(final Multimap<String, File> classMap, final Predicate<String> excludePredicate)
    {
        for (String className : Collections2.filter(classes, Predicates.not(excludePredicate))) {
            classMap.put(className, element);
        }
    }

    void putResources(final Multimap<String, File> resourceMap, final Predicate<String> excludePredicate)
    {
        for (String resource : Collections2.filter(resources, Predicates.not(excludePredicate))) {
            resourceMap.put(resource, element);
        }
    }

    static class Builder
    {
        private final File element;
        private final ImmutableSet.Builder<String> classBuilder = ImmutableSet.builder();
        private final ImmutableSet.Builder<String> resourcesBuilder = ImmutableSet.builder();

        private Builder(final File element)
        {
            this.element = element;
        }

        void addClass(String className)
        {
            classBuilder.add(className);
        }

        void addResource(String resource)
        {
            resourcesBuilder.add(resource);
        }

        ClasspathCacheElement build()
        {
            return new ClasspathCacheElement(element, classBuilder.build(), resourcesBuilder.build());
        }
    }
}
