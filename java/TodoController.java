package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Todo;
import com.example.demo.mapper.TodoMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Validated
public class TodoController {

	private final TodoMapper todoMapper;

	@GetMapping("/")
	public String getAllTodo(@RequestParam(value = "sortColumn", defaultValue = "deadline") String sortColumn,
			@RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder, Model model) {

		List<Todo> todos = todoMapper.getAllTodoSorted(sortColumn, sortOrder);

		model.addAttribute("todo", new Todo());
		model.addAttribute("todos", todos);
		model.addAttribute("sortColumn", sortColumn);
		model.addAttribute("sortOrder", sortOrder);

		model.addAttribute("update", false);
		model.addAttribute("validationError", false);
		// sortUrlメソッドでURLを生成して渡す
		model.addAttribute("sortUrlId", sortUrl("id", sortOrder));
		model.addAttribute("sortUrlTitle", sortUrl("title", sortOrder));
		model.addAttribute("sortUrlImportance", sortUrl("importance", sortOrder));
		model.addAttribute("sortUrlUrgency", sortUrl("urgency", sortOrder));
		model.addAttribute("sortUrlDeadline", sortUrl("deadline", sortOrder));

		return "index";
	}

	public String sortUrl(String column, String order) {
		if (column.equals(order)) {
			return "/?sortColumn=" + column + "&sortOrder=" + (order.equals("asc") ? "desc" : "asc");
		}
		String nextOrder = "asc".equals(order) ? "desc" : "asc";
		return "/?sortColumn=" + column + "&sortOrder=" + nextOrder;
	}

	@GetMapping("/add")
	public String initAddPage(Model model) {
		model.addAttribute("todo", new Todo());
		model.addAttribute("update", false);
		return "add";
	}

	@GetMapping("/delete/{id}")
	public String deleteTodo(@PathVariable("id") Integer id) {
		todoMapper.deleteTodo(id);
		return "redirect:/";
	}
	@GetMapping("/done/{id}")
	public String doneTodo(@PathVariable("id") Integer id) {
		Todo todo = todoMapper.getTodoById(id);
		todo.setDone("Y");
		todoMapper.updateTodo(todo);
		return "redirect:/";
	}

	// 編集画面の表示処理
	@GetMapping("/update/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		Todo todo = todoMapper.getTodoById(id); // Todo情報を取得
		model.addAttribute("todo", todo);
		List<Todo> todos = todoMapper.getAllTodoSorted("deadline", "asc");
		model.addAttribute("todos", todos);
		model.addAttribute("sortColumn", "deadline");
		model.addAttribute("sortOrder", "asc");
		model.addAttribute("update", true); // 編集モーダルを表示するためのフラグ
		return "index"; // indexページに遷移
	}

	// 編集ボタンの処理
	@PostMapping("/update")
	public String updateForm(@Validated Todo todo, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			// バリデーションエラーがあった場合、indexに遷移
			List<Todo> todos = todoMapper.getAllTodoSorted("deadline", "asc");
			model.addAttribute("todo", todo);
			model.addAttribute("todos", todos);
			model.addAttribute("sortColumn", "deadline");
			model.addAttribute("sortOrder", "asc");
			model.addAttribute("update", true); // 編集モーダル用のフラグ
			model.addAttribute("validationError", true); // バリデーションエラーフラグ
			return "index"; // indexページに遷移
		}
		if (todo.getDone() == null) {
			todo.setDone("N"); // デフォルトで未完了とする
		}
		todoMapper.updateTodo(todo);
		return "redirect:/";
	}

	@PostMapping("/add")
	public String submitForm(@Validated Todo todo, BindingResult bindingResult, Model model) {
			model.addAttribute("update", false);
			model.addAttribute("validationError", false);
		if (bindingResult.hasErrors()) {
			List<Todo> todos = todoMapper.getAllTodoSorted("deadline", "asc");
			model.addAttribute("todo", todo);
			model.addAttribute("todos", todos);
			model.addAttribute("sortColumn", "deadline");
			model.addAttribute("sortOrder", "asc");
			model.addAttribute("validationError", true); // エラーフラグ
			return "index"; // モーダル内でエラー表示を行う
		}
		if (todo.getDone() == null) {
			todo.setDone("N"); // デフォルトで未完了とする
		}
		todoMapper.insertTodo(todo);
		return "redirect:/";
	}

}
