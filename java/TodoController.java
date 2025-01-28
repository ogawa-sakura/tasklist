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

import com.example.demo.entity.SortUrl;
import com.example.demo.entity.Todo;
import com.example.demo.mapper.TodoMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Validated
public class TodoController {

	private final TodoMapper todoMapper;


	@GetMapping("/")
	public String getAllTodo(@RequestParam(value = "column", defaultValue = "deadline") String column,
			@RequestParam(value = "order", defaultValue = "asc") String order, Model model) {

		// sort用Urlを生成して渡す
		SortUrl sort = new SortUrl();
		sort.setId(sortUrl(column, order));
		sort.setTitle(sortUrl(column, order));
		sort.setImportance(sortUrl(column, order));
		sort.setUrgency(sortUrl(column, order));
		sort.setDeadline(sortUrl(column, order));
		sort.setColumn(column);
		sort.setOrder(order);
		model.addAttribute("sort", sort);
		
		List<Todo> todos = todoMapper.getAllTodoSorted(column, order);
		
		// 初期表示用の設定
		model.addAttribute("todo", new Todo());
		model.addAttribute("todos", todos);
		model.addAttribute("update", false);
		model.addAttribute("validationError", false);

		return "index";
	}
	
	/**
	 * ソート用URL生成メソッド
	 * @param column	ソート対象カラム
	 * @param order		昇順・降順
	 * @return			ソートURL
	 */
	public String sortUrl(String column, String order) {
		if (column.equals(order)) {
			return "/?sort.column=" + column + "&sort.order=" + (order.equals("asc") ? "desc" : "asc");
		}
		String nextOrder = "asc".equals(order) ? "desc" : "asc";
		return "/?sort.column=" + column + "&sort.order=" + nextOrder;
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
		model.addAttribute("sort.column", "deadline");
		model.addAttribute("sort.order", "asc");
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
			model.addAttribute("sort.column", "deadline");
			model.addAttribute("sort.order", "asc");
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
			model.addAttribute("sort.column", "deadline");
			model.addAttribute("sort.order", "asc");
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
