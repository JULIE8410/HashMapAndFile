package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class Employee {

    // Declare the hashmap to hold the employee data

    static HashMap<String, List<Double>> Employee = new HashMap<>();


    public static void main(String[] args) throws IOException {

        double empNum = 0.0;
        double annualSalary = 0.0;
        String name = "";
        boolean flag = true;

        Scanner ac = new Scanner(System.in);


        transferData();

        while(!(name.equals("ex"))){
            System.out.println("Enter the employee name, type ex to exit : ");
            name = ac.nextLine();
            if(name.equals("ex")){
                break;
            }
            System.out.println("Enter the employee number ");
            empNum = ac.nextDouble();
            ac.nextLine();

            flag = true;

            while(flag){
                try{
                    System.out.println("Enter the employee salary");

                    annualSalary = ac.nextDouble();
                    ac.nextLine();

                    if(annualSalary <= 0){
                        throw  new MyError("AnnualSalary should be greater than zero");
                    }


                    flag = false;

                    // Hashmap에 담음 (name이 key, 나머지 2개는 value)
                    Employee.put(name, new ArrayList<>(Arrays.asList(empNum, annualSalary)));

                }catch(InputMismatchException e){
                    System.out.println("You should input a number");
                    // 이 경우 퍼버 flush 시켜야함
                    ac.nextLine();

                }catch(MyError message){
                    System.out.println(message.getMessage());
                }

            }

        }

        display();
        saveFile();
        System.out.println("Data is saved in the text file");

    }

    public static void transferData(){

        File tempFile = new File("MyEmployeeList.txt");  //여기서는 absolute path 적지말고 파일명만 적도록 약속
        boolean exists = tempFile.exists();  // return true/false 파일 있는지 체크해서 있으면  true 반환함

        String input[];   // 파일 내용 저장할 배열

        try{
            if(exists){

                // 여기서 tempFile에 있는거 데이터 읽을건데 예외발생의 위험이 있으니까 2가지 방법 취할수 있음
                // (1. try/catch 2. 메소드(throws FileNotFoundException) 헤드라인에 구현)
                Scanner ac = new Scanner(tempFile);

                // 파일 읽어온거에서 다음줄이 있으면
                while(ac.hasNextLine()){

                    // 1줄 가져와서 data에 저장함
                    String data = ac.nextLine();

                    // 공백을 기준으로 잘라서 배열에 저장함  input = data.split(" ");
                    input = data.split("\\s");

                    // 데이터 형태는 emp 이름 // emp no // emp sal
                    double empNum = Double.parseDouble(input[1]);

                    // emp sal
                    double empSal = Double.parseDouble(input[2]);

                    //
                    Employee.put(input[0], new ArrayList<>(Arrays.asList(empNum, empSal)));


                }

            }
        }catch (FileNotFoundException e){

            System.out.println("The file does not exist.");   // 예외처리의 장점 : input에서 예상치못한 에러 발생시 프로그램 crash 막을수 있음
        }



    }

    public static void display(){
        int count = Employee.size();

        System.out.println("There are currently " + count + " of employees in your records \n");

        // Employee에서 하나씩 꺼내옴(entry는 1개의 꺼내온 녀석이고 이것은 Key, Value 쌍으로 이루어져있음)
        for(Map.Entry<String, List<Double>> entry : Employee.entrySet()) {

            // 이름은 key라서 getKey() 메소드 써서 가져오면 됨
            System.out.println("Employee Name :" + entry.getKey());

            // emp num & emp sal은 value이지만 배열의 형태로 되어있음
            // get()은 key 값 통해서 매칭되는 value 리턴함
            // 리턴한 entry의 value 중에서 첫번째꺼 가져옴
            System.out.println("Employee Number : \t" + Employee.get(entry.getKey()).get(0));

            System.out.println("Employee Salary : \t" + Employee.get(entry.getKey()).get(1));
        }
    }

    public static void saveFile() throws IOException {
        Path file = Paths.get("MyEmployeeList.txt");
        OutputStream output = new BufferedOutputStream(Files.newOutputStream(file, APPEND, CREATE));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter((output)));
        String data;
        try{
            for(Map.Entry<String, List<Double>> entry : Employee.entrySet()){
                data = entry.getKey() + " " + Employee.get(entry.getKey()).get(0) + " " + Employee.get(entry.getKey()).get(1) + "\n";
                writer.write(data);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        writer.close();
    }
}
