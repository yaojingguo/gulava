/*
 *  Copyright (c) 2015 Dmitry Neverov and Google
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package musubi.processor;

import musubi.annotation.MakePredicates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

/**
 * Metadata necessary to generate a predicates subclass. This contains information obtained from the
 * class annotated with @{@link MakePredicates}.
 */
public final class MakePredicatesMetadata {
  private final String name;
  private final List<Predicate> predicates;
  private final TypeElement annotatedType;

  private MakePredicatesMetadata(
      String name, List<Predicate> predicates, TypeElement annotatedType) {
    this.name = name;
    this.predicates = Collections.unmodifiableList(new ArrayList<>(predicates));
    this.annotatedType = annotatedType;
  }

  /** The name of the generated implementation class. */
  public String getName() {
    return name;
  }

  public List<Predicate> getPredicates() {
    return predicates;
  }

  public TypeElement getAnnotatedType() {
    return annotatedType;
  }

  /**
   * Returns the metadata stored in a type annotated with @{@link MakePredicates}.
   */
  public static MakePredicatesMetadata of(TypeElement annotatedType, Messager messager) {
    String name = "MakePredicates_" + Processors.generatedClassName(annotatedType);
    List<ExecutableElement> predicateMethods = new ArrayList<>();

    List<? extends ExecutableElement> allMethods =
        ElementFilter.methodsIn(annotatedType.getEnclosedElements());
    for (ExecutableElement method : allMethods) {
      if (!method.getModifiers().contains(Modifier.PRIVATE)
          && !method.getModifiers().contains(Modifier.STATIC)
          && method.getModifiers().contains(Modifier.ABSTRACT)) {
        predicateMethods.add(method);
      }
    }

    ClauseMethods clauseMethods = ClauseMethods.withPredicates(predicateMethods, messager);

    for (ExecutableElement method : allMethods) {
      if (method.getModifiers().contains(Modifier.FINAL)
          && Processors.isPackageProtected(method)) {
        clauseMethods.addClause(method);
      }
    }

    return new MakePredicatesMetadata(name, clauseMethods.predicateMetadata(), annotatedType);
  }
}
