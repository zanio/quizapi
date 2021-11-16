package com.demo.quizapi.repository;

import com.demo.quizapi.entities.Option;
import com.demo.quizapi.entities.Question;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OptionRepository extends PagingAndSortingRepository<Option, Integer> { }
