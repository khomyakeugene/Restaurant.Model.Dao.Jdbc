package com.company.restaurant.controllers;

import com.company.restaurant.model.*;
import com.company.util.ObjectService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Yevhen on 20.05.2016.
 */
public class RestaurantControllerTest {
    private final static String APPLICATION_CONTEXT_NAME = "restaurant-controller-application-context.xml";

    private static MenuController menuController;
    private static TableController tableController;
    private static EmployeeController employeeController;
    private static WarehouseController warehouseController;
    private static KitchenController kitchenController;
    private static OrderController orderController;
    private static CourseController courseController;

    private static int closedOrderId;
    private static Order closedOrder;
    private static String closedOrderCourseName1;
    private static Course closedOrderCourse1;
    private static String closedOrderCourseName2;
    private static Course closedOrderCourse2;
    private static Course testCourse;

    private static Employee employee() {
        return employeeController.findAllEmployees().get(0);
    }

    private int jobPositionId() {
        return employeeController.findAllJobPositions().get(0).getId();
    }

    private static int courseCategoryId() {
        return courseController.findAllCourseCategories().get(0).getId();
    }

    private static int employeeId() {
        return employee().getEmployeeId();
    }

    private static int tableId() {
        return tableController.findAllTables().get(0).getTableId();
    }

    private static int lastTableId() {
        List<Table> tableList = tableController.findAllTables();

        return tableList.get(tableList.size()-1).getTableId();
    }

    private static Course prepareTestCourse() {
        testCourse = new Course();
        testCourse.setCategoryId(courseCategoryId());
        testCourse.setName(Util.getRandomString());
        testCourse.setWeight(Util.getRandomFloat());
        testCourse.setCost(Util.getRandomFloat());

        testCourse = courseController.addCourse(testCourse);

        return testCourse;
    }

    private static void delTestCourse() {
        courseController.delCourse(testCourse);
    }

    private static void prepareClosedOrder() throws Exception {
        Order order = new Order();
        order.setTableId(tableId());
        order.setEmployeeId(employeeId());
        order.setOrderNumber(Util.getRandomString());
        closedOrderId = orderController.addOrder(order).getOrderId();

        // Courses for closed order ----------------------------
        closedOrderCourseName1 = Util.getRandomString();
        closedOrderCourse1 = new Course();
        closedOrderCourse1.setCategoryId(courseCategoryId());
        closedOrderCourse1.setName(closedOrderCourseName1);
        closedOrderCourse1.setWeight(Util.getRandomFloat());
        closedOrderCourse1.setCost(Util.getRandomFloat());
        closedOrderCourse1 = courseController.addCourse(closedOrderCourse1);

        closedOrderCourseName2 = Util.getRandomString();
        closedOrderCourse2 = new Course();
        closedOrderCourse2.setCategoryId(courseCategoryId());
        closedOrderCourse2.setName(closedOrderCourseName2);
        closedOrderCourse2.setWeight(Util.getRandomFloat());
        closedOrderCourse2.setCost(Util.getRandomFloat());
        closedOrderCourse2 = courseController.addCourse(closedOrderCourse2);
        // ----------

        orderController.addCourseToOrder(order, closedOrderCourse1, 1);

        closedOrder = orderController.closeOrder(order);
    }

    private static void clearClosedOrder() throws Exception {
        Order order = orderController.findOrderById(closedOrderId);
        // Manually change order state to "open"
        order = orderController.updOrderState(order, "A");

        // Delete "open" order
        orderController.delOrder(order);

        // Delete course for closed order
        courseController.delCourse(closedOrderCourseName1);
        courseController.delCourse(closedOrderCourseName2);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_NAME);

        menuController = applicationContext.getBean(MenuController.class);
        tableController = applicationContext.getBean(TableController.class);
        employeeController = applicationContext.getBean(EmployeeController.class);
        warehouseController = applicationContext.getBean(WarehouseController.class);
        kitchenController = applicationContext.getBean(KitchenController.class);
        orderController = applicationContext.getBean(OrderController.class);
        courseController = applicationContext.getBean(CourseController.class);

        prepareTestCourse();
        prepareClosedOrder();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        delTestCourse();
        clearClosedOrder();

        System.out.println("tearDownClass finished!");
    }

    @Test(timeout = 2000)
    public void addFindDelJobPosition() throws Exception {
        String name = Util.getRandomString();
        JobPosition jobPosition = employeeController.addJobPosition(name);

        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(jobPosition,
                employeeController.findJobPositionByName(jobPosition.getName())));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(jobPosition,
                employeeController.findJobPositionById(jobPosition.getId())));

        employeeController.delJobPosition(name);
        assertTrue(employeeController.findJobPositionByName(name) == null);
        // Test delete of non-existent data
        employeeController.delJobPosition(name);
    }

    @Test//(timeout = 2000)
    public void addFindDelEmployeeTest() throws Exception {
        for (JobPosition jobPosition : employeeController.findAllJobPositions()) {
            System.out.println("Job position Id :" + jobPosition.getId() +
                    ", Job position name :" + jobPosition.getName());
        }


        String firstName = Util.getRandomString();
        String secondName = Util.getRandomString();
        Employee employee = new Employee();
        employee.setJobPositionId(jobPositionId());
        employee.setFirstName(firstName);
        employee.setSecondName(secondName);
        employee.setPhoneNumber(Util.getRandomString());
        employee.setSalary(Util.getRandomFloat());

        employee = employeeController.addEmployee(employee);
        int employeeId = employee.getEmployeeId();

        // Select test <employee> and check
        Employee employeeByFirstName = employeeController.findEmployeeByFirstName(firstName).get(0);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(employee, employeeByFirstName));

        Employee employeeBySecondName = employeeController.findEmployeeBySecondName(secondName).get(0);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(employee, employeeBySecondName));

        Employee employeeByFirstAndSecondName =
                employeeController.findEmployeeByFirstAndSecondName(firstName, secondName).get(0);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(employee, employeeByFirstAndSecondName));

        Employee employeeById = employeeController.findEmployeeById(employeeId);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(employee, employeeById));

        employeeController.delEmployee(employee);
        assertTrue(employeeController.findEmployeeById(employeeId) == null);
        // Test delete of non-existent data
        employeeController.delEmployee(employee);
        employeeController.delEmployee(employee.getEmployeeId());
    }

    @Test(timeout = 2000)
    public void addFindDelCourseCategoryTest() throws Exception {
        String name = Util.getRandomString();
        CourseCategory courseCategory = courseController.addCourseCategory(name);

        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(courseCategory,
                courseController.findCourseCategoryByName(courseCategory.getName())));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(courseCategory,
                courseController.findCourseCategoryById(courseCategory.getId())));

        courseController.delCourseCategory(name);
        assertTrue(courseController.findCourseCategoryByName(name) == null);
        // Test delete of non-existent data
        courseController.delCourseCategory(name);
    }

    @Test(timeout = 2000)
    public void addFindDelCourseTest() throws Exception {
        String name = Util.getRandomString();
        Course course = new Course();
        course.setCategoryId(courseCategoryId());
        course.setName(name);
        course.setWeight(Util.getRandomFloat());
        course.setCost(Util.getRandomFloat());
        course = courseController.addCourse(course);

        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(course,
                courseController.findCourseByName(course.getName())));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(course,
                courseController.findCourseById(course.getCourseId())));

        courseController.delCourse(name);
        assertTrue(courseController.findCourseByName(name) == null);
        // Test delete by "the whole object"
        course = courseController.addCourse(course);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(course,
                courseController.findCourseByName(name)));
        courseController.delCourse(course);
        assertTrue(courseController.findCourseByName(name) == null);
        // Test delete of non-existent data
        courseController.delCourse(name);

        // Whole course list
        courseController.findAllCourses();
    }

    @Test(timeout = 2000)
    public void addFindDelMenuTest() throws Exception {
        String name = Util.getRandomString();
        Menu menu = menuController.addMenu(name);

        Menu menuByName = menuController.findMenuByName(name);
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(menu, menuByName));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(menu,
                menuController.findMenuById(menu.getId())));

        // Courses in menu ----------------------------
        String courseName1 = Util.getRandomString();
        Course course1 = new Course();
        course1.setCategoryId(courseCategoryId());
        course1.setName(courseName1);
        course1.setWeight(Util.getRandomFloat());
        course1.setCost(Util.getRandomFloat());
        course1 = courseController.addCourse(course1);

        String courseName2 = Util.getRandomString();
        Course course2 = new Course();
        course2.setCategoryId(courseCategoryId());
        course2.setName(courseName2);
        course2.setWeight(Util.getRandomFloat());
        course2.setCost(Util.getRandomFloat());
        course2 = courseController.addCourse(course2);

        menuController.addCourseToMenu(menu, course1);
        menuController.addCourseToMenu(menu, course2);

        for (MenuCourseList menuCourseList : menuController.findMenuCourses(menu)) {
            menuController.findMenuCourseByCourseId(menu, menuCourseList.getCourseId());
            System.out.println(menuCourseList.getCourseName() + ": " + menuCourseList.getCourseCategoryName());
        }


        menuController.delCourseFromMenu(menu, course1);
        menuController.delCourseFromMenu(menu, course2);

        courseController.delCourse(courseName1);
        courseController.delCourse(courseName2);
        // ----------------------------

        for (Menu m : menuController.findAllMenus()) {
            System.out.println("menu_id: " + m.getId() + ", name: " + m.getName());
        }

        menuController.delMenu(name);
        assertTrue(menuController.findMenuByName(name) == null);
        // Test delete of non-existent data
        menuController.delMenu(menu);
    }

    @Test(timeout = 2000)
    public void addFindDelTableTest() throws Exception {
        Table table = new Table();
        table.setNumber(tableController.findTableById(lastTableId()).getNumber() + Util.getRandomInteger());
        table.setDescription(Util.getRandomString());
        table = tableController.addTable(table);

        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(table,
                tableController.findTableByNumber(table.getNumber())));
        assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(table,
                tableController.findTableById(table.getTableId())));

        tableController.delTable(table);
        assertTrue(tableController.findTableByNumber(table.getNumber()) == null);
    }

    @Test (timeout = 2000)
    public void addFindDelOrderTest() throws Exception {
        Order order = new Order();
        order.setTableId(tableId());
        order.setEmployeeId(employeeId());
        order.setOrderNumber(Util.getRandomString());
        int orderId = orderController.addOrder(order).getOrderId();

        Order orderById = orderController.findOrderById(orderId);
        // Just check of successful retrieving from database,  without "full comparing"!!!
        // Because, at least field <order_datetime> is filling by default (as a current timestamp) on the database level
        assertTrue(orderById != null);

        // Courses in order ----------------------------
        String courseName1 = Util.getRandomString();
        Course course1 = new Course();
        course1.setCategoryId(courseCategoryId());
        course1.setName(courseName1);
        course1.setWeight(Util.getRandomFloat());
        course1.setCost(Util.getRandomFloat());
        course1 = courseController.addCourse(course1);

        String courseName2 = Util.getRandomString();
        Course course2 = new Course();
        course2.setCategoryId(courseCategoryId());
        course2.setName(courseName2);
        course2.setWeight(Util.getRandomFloat());
        course2.setCost(Util.getRandomFloat());
        course2 = courseController.addCourse(course2);

        orderController.addCourseToOrder(order, course1, 3);
        orderController.addCourseToOrder(order, course2, 2);

        for (OrderCourse orderCourse : orderController.findAllOrderCourses(order)) {
            orderController.findOrderCourseByCourseId(order, orderCourse.getCourseId());
            System.out.println(orderCourse.getCourseName() + " : " + orderCourse.getCourseCost());
        }

        orderController.takeCourseFromOrder(order, course1, 2);
        orderController.takeCourseFromOrder(order, course1);
        orderController.takeCourseFromOrder(order, course2, 2);

        courseController.delCourse(courseName1);
        courseController.delCourse(courseName2);
        // ----------------------------

        for (Order o : orderController.findAllOrders()) {
            System.out.println("Order id: " + o.getOrderId() + ", Order number: " + o.getOrderNumber());
        }

        for (Order o : orderController.findAllOpenOrders()) {
            System.out.println("Open order id: " + o.getOrderId() + ", Order number: " + o.getOrderNumber());
        }

        for (Order o : orderController.findAllClosedOrders()) {
            System.out.println("Closed order id: " + o.getOrderId() + ", Order number: " + o.getOrderNumber());
        }

        orderController.delOrder(order);
        assertTrue(orderController.findOrderById(orderId) == null);
    }

    @Test(timeout = 2000)
    public void closedOrderTest_1() throws Exception {
        // <DataIntegrityException> should be generated next
        orderController.delOrder(closedOrder);
    }

    @Test(timeout = 2000)
    public void closedOrderTest_2() throws Exception {
        // <DataIntegrityException> should be generated next
        orderController.addCourseToOrder(closedOrder, closedOrderCourse2, 1);
    }

    @Test(timeout = 2000)
    public void closedOrderTest_3() throws Exception {
        // <DataIntegrityException> should be generated next
        orderController.takeCourseFromOrder(closedOrder, closedOrderCourse1, 1);
    }

    @Test(timeout = 2000)
    public void addCookedCourse() throws Exception {
        kitchenController.addCookedCourse(testCourse, employee(), Util.getRandomFloat());

        for (CookedCourse cookedCourse : kitchenController.findAllCookedCourses()) {
            System.out.println(cookedCourse.getCourseName() + " : " + cookedCourse.getCookDatetime());
        }
    }

    @Test(timeout = 10000)
    public void addFindDelWarehouseTest() throws Exception {
        for (Ingredient ingredient: warehouseController.findAllIngredients()) {
            for (Portion portion : warehouseController.findAllPortions()) {
                warehouseController.addIngredientToWarehouse(ingredient, portion, Util.getRandomFloat());
                warehouseController.takeIngredientFromWarehouse(ingredient, portion, Util.getRandomFloat());

                System.out.println("menuController.findPortionById(" + portion.getPortionId() + ") test ...");
                assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(portion,
                        warehouseController.findPortionById(portion.getPortionId())));
            }

            System.out.println("menuController.findIngredientById(" + ingredient.getId() + ") test ...");
            assertTrue(ObjectService.isEqualByGetterValuesStringRepresentation(ingredient,
                    warehouseController.findIngredientById(ingredient.getId())));

            System.out.println("Warehouse: " + ingredient.getName() + " : ");
            for (Warehouse warehouse : warehouseController.findIngredientInWarehouseByName(ingredient.getName())) {
                System.out.println(warehouse.getPortionDescription() + ": " + warehouse.getAmount());
            }
        }

        System.out.println("Warehouse all ingredients:");
        for (Warehouse warehouse : warehouseController.findAllWarehouseIngredients()) {
            System.out.println(warehouse.getIngredientName() + ": " + warehouse.getAmount());
        }
        System.out.println("Warehouse elapsing ingredients:");
        for (Warehouse warehouse : warehouseController.findAllElapsingWarehouseIngredients((float)500.0)) {
            System.out.println(warehouse.getIngredientName() + ": " + warehouse.getPortionDescription() + ": " +
                    warehouse.getAmount());
        }
    }
}