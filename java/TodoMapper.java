package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Todo;

@Mapper
public interface TodoMapper {
	/* idで1件取得 */
	Todo getTodoById(int id);
	/* リスト全件取得 */
	List<Todo> getAllTodo();
	/* タスク登録 */
	void insertTodo(Todo todo);
	/* 更新 */
	void updateTodo(Todo todo);
	/* タスク削除 */
	void deleteTodo(int id);
	// ソートされたTodoリストを取得するメソッド
	List<Todo> getAllTodoSorted(@Param("column") String column, @Param("order") String order);
}
