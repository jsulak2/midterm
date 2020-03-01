package sample;

public class RandomNums
{
    public String randomNum;

    public void generateRandomNum()
    {
        double num;
        num = Math.random()*10;
        Math.round(num);
        randomNum = (num + "");
    }
    public void generateRandomNum(int lowNum, int hiNum)
    {
        double num;
        num = Math.random()*hiNum+1;
        while (num<lowNum)
            num = Math.random()*hiNum+1;
        num=Math.floor(num);
        randomNum = (num + "");
    }

    @Override
    public String toString(){
        return (this.randomNum);
    }

}
