package game.SpringBoot.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

public class TestProtobuf
{

	public static void main(String[] args)
	{
		ProtoBuf.Person.Builder builder = ProtoBuf.Person.newBuilder();
		builder.setName("daxiong");
		builder.setAge(30);
		
		ProtoBuf.Person person = builder.build();
		byte[] buf = person.toByteArray();
		
		try
		{
			ProtoBuf.Person person2 = ProtoBuf.Person.parseFrom(buf);
			System.out.println(person2.getName() + ", " + person2.getAge());
		}
		catch (InvalidProtocolBufferException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
